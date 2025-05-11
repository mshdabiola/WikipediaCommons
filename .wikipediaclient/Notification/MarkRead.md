```toml
name = 'MarkRead'
method = 'GET'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=echomarkread'
sortWeight = 2000000
id = '21c02b57-d272-48f8-99f2-916606f06991'

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
value = 'echomarkread'

[[body.urlEncoded]]
key = 'token'

[[body.urlEncoded]]
key = 'unreadlist'

[[body.urlEncoded]]
key = 'unreadlist'
```
