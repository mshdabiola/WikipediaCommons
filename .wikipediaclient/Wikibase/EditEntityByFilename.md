```toml
name = 'EditEntityByFilename'
method = 'POST'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=wbeditentity&site=commonswiki'
sortWeight = 2000000
id = '181f38df-3db4-45a0-92a6-afbbe0790ced'

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

[[queryParams]]
key = 'site'
value = 'commonswiki'

[[body.urlEncoded]]
key = 'title'

[[body.urlEncoded]]
key = 'token'

[[body.urlEncoded]]
key = 'data'
```
