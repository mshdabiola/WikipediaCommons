```toml
name = 'PostAppend'
method = 'POST'
url = '{{WikiDataUrlWithData}}action=edit'
sortWeight = 3000000
id = 'd8c06401-431c-48ae-bb68-a08172bc106f'

[[body.urlEncoded]]
key = 'title'

[[body.urlEncoded]]
key = 'summary'

[[body.urlEncoded]]
key = 'appendtext'

[[body.urlEncoded]]
key = 'token'
```
