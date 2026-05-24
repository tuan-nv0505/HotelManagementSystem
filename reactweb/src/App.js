import { BrowserRouter, Route, Routes } from "react-router-dom"
import Header from "./components/Header"
import Footer from "./components/Footer"
import Home from "./screens/Home/Home"
// import Login from "./screens/User/Login"
// import Register from "./screens/User/Register"
// import LoginModal from "./screens/User/LoginModal"
// import RegisterModal from "./screens/User/RegisterModal"

const App = () => {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        {/* <Route path="/login" element={<LoginModal />} />
        <Route path="/register" element={<RegisterModal />} /> */}
      </Routes>
      <Footer />
    </BrowserRouter>
  )
}

export default App; 
