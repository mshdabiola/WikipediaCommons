```toml
name = 'FileToStash'
method = 'POST'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=upload&stash=1&ignorewarnings=1'
sortWeight = 1000000
id = '95ff4f5a-8b5e-4680-9111-fa01c9234bda'

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
value = 'upload'

[[queryParams]]
key = 'stash'
value = '1'

[[queryParams]]
key = 'ignorewarnings'
value = '1'

[[body.formData]]
key = 'filename'

[[body.formData]]
key = 'filesize'

[[body.formData]]
key = 'offset'

[[body.formData]]
key = 'filekey'

[[body.formData]]
key = 'token'

[[body.formData]]
type = 'FILE'
key = 'filePart'
```
