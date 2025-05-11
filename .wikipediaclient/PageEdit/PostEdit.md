```toml
name = 'PostEdit'
method = 'POST'
url = '{{WikiDataUrlWithData}}action=edit'
sortWeight = 1000000
id = '18c27632-8f93-4884-aa94-41cbf600924a'

[[body.urlEncoded]]
key = 'title'

[[body.urlEncoded]]
key = 'summary'

[[body.urlEncoded]]
key = 'text'

[[body.urlEncoded]]
key = 'token'
```
