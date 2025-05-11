```toml
name = 'EditEntity'
method = 'POST'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=wbeditentity'
sortWeight = 1000000
id = '2b438510-62a2-4b4b-b9d7-39536560560b'

[[queryParams]]
key = 'format'
value = 'json'

[[queryParams]]
key = 'formatversion'
value = '2'

[[queryParams]]
key = 'errorformat'
value = 'plaintext'

[[queryParams]]
key = 'action'
value = 'wbeditentity'

[[body.urlEncoded]]
key = 'id'

[[body.urlEncoded]]
key = 'token'

[[body.urlEncoded]]
key = 'data'
```
