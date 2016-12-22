import React, { Component } from 'react'
import * as d3 from 'd3'

import './App.css'

import ScatterPlotComponent from './ScatterPlotComponent'

class App extends Component {

  constructor(props) {
    super(props)
    this.state = {
      data : [],
      xVar : 'fertility_rate',
      yVar : 'life_expectancy',
      idVar : 'country',
      search : ''
    }
  }

  componentWillMount() {
    // Get initial data
    d3.csv('data/prepped_data_min.csv', (data) => {
      console.log('setting data: ', data)
      this.setState({data: data})
    })
  }

  componentDidMount() {
    const ws = new WebSocket('ws://localhost:8083/register?name=React')
    ws.onopen = function (event) {
      console.log('Websocket conenction is now open. Ready to receive message.')
    }
    ws.onmessage = (event) => {
      var currentState = this.state.data
      currentState.push(JSON.parse(event.data))
      this.setState({data: currentState})
    }
  }

  changeX(event, index, value) {
    this.setState({xVar:value})
  }

  changeY(event, index, value) {
    this.setState({yVar:value})
  }

  search(event) {
    this.setState({search:event.target.value.toLowerCase()})
  }

  render() {
    // Prep data
    let charData = this.state.data.map((d) => {
      let selected = d[this.state.idVar].toLowerCase().match(this.state.search) !== null
      console.log(selected)
      return {
        x : d[this.state.xVar],
        y : d[this.state.yVar],
        id : d[this.state.idVar],
        selected : selected
      }
    })

    let titleMap = {
      gdp : 'Gross Domestic Product',
      life_expectancy : 'Life expectancy in 2014',
      fertility_rate : 'Fertility Rate'
    }

    let titles = {
      x : titleMap[this.state.xVar],
      y : titleMap[this.state.yVar]
    }

    return (
      <div>
        <h1 className="header">WebSocket Demo</h1>

        {this.state.data.length !== 0 &&
          <div className="App">
            <ScatterPlotComponent
              xTitle={titles.x}
              yTitle={titles.y}
              search={this.state.search}
              data={charData}
              width={window.innerWidth * .7}
              height={window.innerHeight - 220} />
          </div>
        }
      </div>

    )
  }
}

export default App
