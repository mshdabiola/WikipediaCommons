```toml
name = 'PostCreate'
method = 'POST'
url = '{{WikiDataUrlWithData}}action=edit'
sortWeight = 2000000
id = 'c7cdcace-5458-4379-a437-406ca43edda2'

[[body.urlEncoded]]
key = 'title'

[[body.urlEncoded]]
key = 'summary'

[[body.urlEncoded]]
key = 'text'

[[body.urlEncoded]]
key = 'contentformat'

[[body.urlEncoded]]
key = 'contentmodel'

[[body.urlEncoded]]
key = 'minor'

[[body.urlEncoded]]
key = 'recreate'

[[body.urlEncoded]]
key = 'token'
```
