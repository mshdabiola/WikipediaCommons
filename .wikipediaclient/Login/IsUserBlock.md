```toml
name = 'IsUserBlock'
method = 'GET'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=query&meta=userinfo&uiprop=blockinfo'
sortWeight = 7000000
id = '00ebde01-7359-44c5-8627-0595c6ce0091'

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
value = 'query'

[[queryParams]]
key = 'meta'
value = 'userinfo'

[[queryParams]]
key = 'uiprop'
value = 'blockinfo'
```
