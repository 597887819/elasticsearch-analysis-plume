[TOC]



# Plume ES 分词插件

一个自定义的elasticsearch分词器，主要参考了 tenlee的 [elasticsearch-analysis-demo](https://github.com/tenlee2012/elasticsearch-analysis-demo) 框架。

目前提供了 h_gram（从头部开始） 和 t_gram（从尾部开始）2种分词策略。

h_gram可用于解决 x like "ab%" 的模糊查询，t_gram 则反之用于解决 x like "%ab" 的模糊查询



## 使用方式

### 获取插件包

#### 方法一

打包 `mvn clean package -DskipTests=true`

编译成功后，将会生成打包好的插件压缩包：`target/releases/analysis-plume-{版本号}.zip`。

#### 方法二

### 安装插件包

将 analysis-plume-{版本号}.zip 解压到 elasticsearch 的 plugin 目录

`unzip analysis-plume-{版本号}.zip -d plume`

注意unzip不会删除源文件，需要手动移除，否则启动报错

`rm -rf analysis-plume-{版本号}.zip`

### 重启ES

`kill -SIGTERM {pid}`

`sh {es目录}/bin/elasticsearch -d`

### 配置索引

将分词器配置到具体索引的setting中，并在特定字段使用它

```json
"analysis": {
    "analyzer": {
      "t_gram" : {
        "tokenizer": "t_gram"
      },
      "h_gram" : {
        "tokenizer": "h_gram"
      },
      "none_gram" : {
        "tokenizer": "none_gram"
      }
    },
    "tokenizer": {
      "t_gram": {
        "type": "plume",
        "strategy": "t_gram",
        "min_gram" : "1",
        "max_gram" : "8"
      },
      "h_gram": {
        "type": "plume",
        "strategy": "h_gram",
        "min_gram" : "4",
        "max_gram" : "10"
      },
      "none_gram":{
        "type": "plume",
        "strategy": "none_gram"
      }
    }
  }
```

如代码所示支持3种策略

| 策略      | 说明                                                         |
| --------- | ------------------------------------------------------------ |
| t_gram    | 即 tail-gram 表示从尾部开始分词 可用于尾部匹配 如mysql的like "%xxx"<br/>如：SE1586 被分为 [6,86,586,1586,E1586,SE1586] |
| h_gram    | 即  head-gram 表示从头部开始分词 可用于头部匹配 如mysql *的* like "xxx%"<br/>如：SE1586 被分为 [S,SE,SE1,SE15,SE158,SE1586] |
| none_gram | 不分词                                                       |



## 实例

### 创建索引

索引的setting中需要设置好自定义分词器

```json
# 删除索引
DELETE demo_index

# 创建索引
PUT demo_index
{
  "settings": {
    "number_of_replicas": 0,
    "number_of_shards" : 1,
    "index" : {
      "analysis": {
        "analyzer": {
          "t_gram" : {
            "tokenizer": "t_gram"
          },
          "h_gram" : {
            "tokenizer": "h_gram"
          },
          "none_gram" : {
            "tokenizer": "none_gram"
          }
        },
        "tokenizer": {
          "t_gram": {
            "type": "plume",
            "strategy": "t_gram",
            "min_gram" : "1",
            "max_gram" : "8"
          },
          "h_gram": {
            "type": "plume",
            "strategy": "h_gram",
            "min_gram" : "4",
            "max_gram" : "10"
          },
          "none_gram":{
            "type": "plume",
            "strategy": "none_gram"
          }
        }
      }
    }
  },
  "mappings": {
    "default" : {
      "properties" : {
        "trade_no": {
          "type": "keyword"
        },
        "trade_no_tgram": {
          "type": "text", 
          "analyzer": "t_gram"
        },
        "trade_no_hgram": {
          "type": "text",
          "analyzer": "h_gram"
        }
      }
    }
  }
}

# 查看settings 与 mapping
GET demo_index/_settings
GET demo_index/_mapping
```

### 批量写入数据

```json
POST /demo_index/_doc/_bulk
{ "index": { "_id": 1 }}
{ "trade_no" : "31002307081363661586","trade_no_tgram":"31002307081363661586","trade_no_hgram":"31002307081363661586"}
{ "index": { "_id": 2 }}
{ "trade_no" : "31002307081363661587","trade_no_tgram":"31002307081363661587","trade_no_hgram":"31002307081363661587"}
{ "index": { "_id": 3 }}
{ "trade_no" : "41002307081363661586","trade_no_tgram":"41002307081363661586","trade_no_hgram":"41002307081363661586"}
```

### 测试

```json
# 指定查询分词器为none_gram 查询字符串作为整体，能召回id为1,3的数据
GET demo_index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "trade_no_tgram": {
              "analyzer": "none_gram", 
              "query": "63661586"
            }
          }
        }
      ]
    }
  }
}

# 不指定查询分词器则使用字段的分词器 t_gram，只做了[1,8]尾部窗口截取，无法召回数据
GET demo_index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "trade_no_tgram": {
              "query": "070813"
            }
          }
        }
      ]
    }
  }
}

# 指定查询分词器为none_gram 查询字符串作为整体，由于 h_gram，做了[4,10]头窗口截取，可召回数据id为2的数据
GET demo_index/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "trade_no_hgram": {
              "analyzer": "none_gram", 
              "query": "4100230708"
            }
          }
        }
      ]
    }
  }
}
```



## 背景

而面对英文elasticsearch利用内置的Standard、Simple、Keywod Analyzers，同时配合 Lowercase、Whitespace、NGram Tokenizers 一般能满足大部分场景。

面对中文或者说中英文混合的场景市面上也有很多elasticsearch分词器，它们基本采用特定算法模型+字典的方式进行分词。
比如 [ik](https://github.com/medcl/elasticsearch-analysis-ik)、[jieba](https://github.com/sing1ee/elasticsearch-jieba-plugin)、[thulac](https://github.com/microbun/elasticsearch-thulac-plugin)、[Ansj](https://github.com/NLPchina/elasticsearch-analysis-ansj)、[HanLP](https://github.com/KennFalcon/elasticsearch-analysis-hanlp) 

但是很多时候elasticsearch的使用方很有可能是业务开发，他们更熟悉SQL方式的查询。比如：x like "%ab"
面对这类模糊查询 elasticsearch 官方提供了 [Wildcard Query](https://www.elastic.co/guide/en/elasticsearch/reference/6.8/query-dsl-wildcard-query.html) 如果有幸运儿使用了这个功能一定会发现该功能不仅查询效率奇差，且对elasticsearch影响极大，可严重至集群宕机（ 网络上随便一搜就能找到诸如《 [Elasticsearch 警惕使用 wildcard 检索！然后呢？](https://zhuanlan.zhihu.com/p/366786960)》的文章）。

elasticsearch 在7.9版本增加了wildcard 类型来解决这个问题，但是elasticsearch集群升级的成本是很高的。那么7.9以下的版本要解决类似SQL的模糊查询就只剩下了 [NGram](https://www.elastic.co/guide/en/elasticsearch/reference/6.8/analysis-ngram-tokenizer.html) 的方式。

NGram的原理是将字符串分割为1~N的窗口，比如：SE7764，按照NGram默认的分词，结果为\[S,E,7,6,4,SE,E7,77,76,64,SE7,E77,776,764,SE77,E776,7764,SE776,E7764,SE7764\] 共20个结果，所以使用这种方式分词时可以满足 x like "%ab%" 的场景的。

缺点也很明显，上面的字符串只有6位就需要20个term，一旦字段长度比较长 NGram分词占用的存储空间也比较恐怖的。而我这边遇到的真实场景是亿级的索引，字段长度有24位。实测容量为原来的2.94倍，而原本该索引就占用了340G的空间（1主1副本），所以采用NGram存储开销太高了。

而真实业务需求只需要 x like "%ab" 查询即可。

通过搜索引擎并未看到有相似的业务场景故此，开发一个ES分词插件

plume的意思是羽毛，希望功能尽可能得轻量（轻如鸿毛），世面上已经有各种算法模型+字典的分词器了使用好就行，没必要重复造轮子，使用尽量简单的方式进行分词，完善主流分词器没有做到的边边角角即可。



## 其它参考资料

6.8版本的 [Help for plugin authors](https://www.elastic.co/guide/en/elasticsearch/plugins/6.8/plugin-authors.html)

最新8.9版本的[Creating text analysis plugins with the stable plugin API](https://www.elastic.co/guide/en/elasticsearch/plugins/master/creating-stable-plugins.html)



## 待办事项

- [ ] rest服务
- [ ] cat服务
- [ ] _analyze支持
