import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from "@pages/Login"
import Register from '@pages/Register'
import VerificationCode from '@pages/VerificationCode'
import RequestHae from '@pages/RequestHae'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path='/login' element={<Login />} />
        <Route path='/register' element={ <Register /> }/>
        <Route path='/verificationCode' element={ <VerificationCode /> }/>
        <Route path='/requestHae' element={ <RequestHae /> } />
      </Routes>
    </BrowserRouter>
  )
}

export default App