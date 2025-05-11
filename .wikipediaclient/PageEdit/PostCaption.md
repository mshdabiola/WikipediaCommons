```toml
name = 'PostCaption'
method = 'POST'
url = "{{WikiDataUrlWithData}}action=wbsetlabel&format=json&site=commonswiki&formatversion=2\n{{WikiDataUrlWithData}} \n{{WikiDataUrlWithData}} \n{{WikiDataUrlWithData}}"
sortWeight = 6000000
id = '2e739c9e-7621-4ff3-acb9-7914df69741d'

[[body.urlEncoded]]
key = 'summary'

[[body.urlEncoded]]
key = 'title'

[[body.urlEncoded]]
key = 'language'

[[body.urlEncoded]]
key = 'value'

[[body.urlEncoded]]
key = 'token'
```
