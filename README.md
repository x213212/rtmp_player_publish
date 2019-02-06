# rtmp_player_publish
https://github.com/qingkouwei/oarplayer

https://github.com/pedroSG94/rtmp-rtsp-stream-client-java

ffmpeg example

ffmpeg -re -i rtmp://localhost:1936/live/myCamera -r 60 -vcodec copy  -preset ultrafast -tune  zerolatency  -filter_complex aresample=44100  -bufsize 1000 -c:a aac -b:a:0 128k -f flv rtmp://localhost:1935/live/movie


