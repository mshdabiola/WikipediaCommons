```toml
name = 'AddLabelToWikiData'
method = 'POST'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=wbsetlabel'
sortWeight = 4000000
id = '3e6862de-3562-4415-981c-1cb3832d9cd3'

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
value = 'wbsetlabel'

[[body.urlEncoded]]
key = 'id'

[[body.urlEncoded]]
key = 'token'

[[body.urlEncoded]]
key = 'language'

[[body.urlEncoded]]
key = 'value'
```
