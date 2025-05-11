```toml
name = 'PostClaim'
method = 'POST'
url = '{{BaseUrl}}?format=json&action=wbsetclaim'
sortWeight = 1000000
id = 'e0a0ed1b-02ef-4f1b-8d7d-fabbdbf3cae5'

[[queryParams]]
key = 'format'
value = 'json'

[[queryParams]]
key = 'action'
value = 'wbsetclaim'

[[body.urlEncoded]]
key = 'claim'

[[body.urlEncoded]]
key = 'tags'

[[body.urlEncoded]]
key = 'token'
```
