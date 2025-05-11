```toml
name = 'NewSection'
method = 'POST'
url = '{{WikiDataUrlWithData}}action=edit&section=new'
sortWeight = 5000000
id = '4eebe8b2-0540-4a3b-8ce4-4daf790e3eed'

[[body.urlEncoded]]
key = 'title'

[[body.urlEncoded]]
key = 'summary'

[[body.urlEncoded]]
key = 'sectiontitle'

[[body.urlEncoded]]
key = 'text'

[[body.urlEncoded]]
key = 'token'
```
