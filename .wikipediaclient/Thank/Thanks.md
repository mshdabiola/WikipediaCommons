```toml
name = 'Thanks'
method = 'POST'
url = '{{BaseUrl}}{{MW_API_PREFIX}}action=thank'
sortWeight = 1000000
id = '24bb86a9-723f-4749-b03a-d002929e50f4'

[[body.urlEncoded]]
key = 'rev'

[[body.urlEncoded]]
key = 'log'

[[body.urlEncoded]]
key = 'token'

[[body.urlEncoded]]
key = 'source'
```
