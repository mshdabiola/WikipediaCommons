```toml
name = 'AllNoti'
method = 'GET'
url = '{{BaseUrl}}?format=json&formatversion=2&errorformat=plaintext&action=query&meta=notifications&notformat=model&notlimit=max&notwikis=wikidatawiki|commonswiki|enwiki&notfilter=read&notcontinue=null'
sortWeight = 1000000
id = 'caa8a5eb-4e59-44eb-be9f-a0053662d89d'

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
value = 'notifications'

[[queryParams]]
key = 'notformat'
value = 'model'

[[queryParams]]
key = 'notlimit'
value = 'max'

[[queryParams]]
key = 'notwikis'
value = 'wikidatawiki|commonswiki|enwiki'

[[queryParams]]
key = 'notfilter'
value = 'read'

[[queryParams]]
key = 'notcontinue'
value = 'null'

[[queryParams]]
key = 'notfilter'
value = '!read'
disabled = true
```
