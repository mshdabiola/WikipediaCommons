```toml
name = 'GetMediaSuppressErrors'
method = 'GET'
url = '{{BaseUrl}}?action=query&format=json&formatversion=2&clprop=hidden&prop=categories|imageinfo&iiprop=url|extmetadata|user&iiurlwidth=640&iiextmetadatafilter=DateTime|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl&titles=Nigeria'
sortWeight = 9000000
id = 'fc8779ac-7194-4e65-b267-5b0193df33ae'

[[queryParams]]
key = 'action'
value = 'query'

[[queryParams]]
key = 'format'
value = 'json'

[[queryParams]]
key = 'formatversion'
value = '2'

[[queryParams]]
key = 'clprop'
value = 'hidden'

[[queryParams]]
key = 'prop'
value = 'categories|imageinfo'

[[queryParams]]
key = 'iiprop'
value = 'url|extmetadata|user'

[[queryParams]]
key = 'iiurlwidth'
value = '640'

[[queryParams]]
key = 'iiextmetadatafilter'
value = 'DateTime|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl'

[[queryParams]]
key = 'titles'
value = 'Nigeria'

[[headers]]
key = 'x-commons-suppress-error-log'
value = 'true'
```
