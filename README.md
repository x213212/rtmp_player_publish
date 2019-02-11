# rtmp_player_publish

##參考

https://github.com/pedroSG94/rtmp-rtsp-stream-client-java

https://github.com/qingkouwei/oarplayer

https://github.com/gwuhaolin/livego

## ffmpeg example 示範
address1 adobe_media_server

address2 livego


ffmpeg -re -i rtmp:// address1 -r 60 -vcodec libx264 -s 640x480 -preset ultrafast -tune  zerolatency  -filter_complex aresample=44100  -bufsize 1000 -c:a aac -b:a:0 128k -f flv rtmp:// address2


![alt text](https://i.imgur.com/WahFcc2.png)
