# rtmp_player_publish

An Android app that both **publishes** and **plays** RTMP streams: capture from
the device camera and push to a server, or pull a stream and render it.

Built for a surgical video-streaming and conferencing system, where the same
handset needs to send its own feed and watch someone else's.

## Build

```bash
./gradlew assembleDebug
```

Install with `./gradlew installDebug`, or open the project in Android Studio.

## Layout

The RTMP protocol layer is implemented in Java rather than wrapped from native
code — `AmfData`, `AmfArray`, `AmfBoolean` and friends encode AMF0, and
`AacPacket`, `Acknowledgement`, `Abort` implement the RTMP chunk and control
messages. `AbstractMediaPlayer` and `AdapterBitrateParser` sit on the playback
side, the latter adapting bitrate to the measured link.

## Derived from

- [pedroSG94/rtmp-rtsp-stream-client-java](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java)
- [qingkouwei/oarplayer](https://github.com/qingkouwei/oarplayer)
- [gwuhaolin/livego](https://github.com/gwuhaolin/livego) (server side)

## License

Apache-2.0, inherited from the upstream projects above. See [LICENSE.txt](LICENSE.txt).
