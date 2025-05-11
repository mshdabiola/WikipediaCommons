```toml
name = 'UploadFromStash'
method = 'POST'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=upload&stash=1&ignorewarnings=1'
sortWeight = 2000000
id = '3a57ef33-884c-4a5d-9740-25a4ce190d44'

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

[[headers]]
key = 'Cache-Control'
value = 'no-cache'

[[body.urlEncoded]]
key = 'token'

[[body.urlEncoded]]
key = 'text'

[[body.urlEncoded]]
key = 'comment'

[[body.urlEncoded]]
key = 'filename'

[[body.urlEncoded]]
key = 'filekey'
```
