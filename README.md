# Akka HTTP based Websocket with React and D3

This is a simple application showing how you can create a D3 based chart updating in real time. It utilizes WebSockets for real time updates.
The WebSocket server has been implemented using [Akka HTTP](http://doc.akka.io/docs/akka-http/current/scala.html) and the User Interface has been built using [React](https://facebook.github.io/react/) and [D3](https://d3js.org/).

The application is loosely based on the following flow:
* `client` The React App spins up a D3 based scatter plot based on a file content, which contains around 10 records. On component mounting, it opens up a WebSocket client and registers itself to the server
to receive new data
* `server` An [Akka HTTP](http://doc.akka.io/docs/akka-http/current/scala.html) based server listens on a WebSocket endpoint. On getting a registration request, it creates a stream with the remaining file as the Source and the WebSocket as the Sink.
 The websocket sink receives a line from the file every 3 seconds(configurable)
* `client` The React App receives the new data, which then triggers an update to the underlying component consisting the graph resulting in re-scaling and re-drawing of the chart

## Run the application
```shell
$ git clone https://github.com/nitishkr88/akka-http-websocket.git
$ cd akka-http-websocket/d3-react-websocket
$ npm install
$ npm run build

$ cd.. (home directory)
$ sbt
> ~re-start(for hot reload) or run
$ open http://localhost:8083
watch the chart update every 3-4 seconds....
```

## IDE integration

### Intellij

In Intellij, open Project wizard, select `Import Project`, choose the root folder and click `OK`.
Select `Import project from external model` option, choose `SBT project` and click `Next`. Select additional import options and click `Finish`.
Make sure you use the Intellij Scala Plugin v1.3.3 or higher. There are known issues with prior versions of the plugin.
