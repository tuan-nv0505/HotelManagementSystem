import axios from "axios";
import cookies from 'react-cookies'

export const endpoints = {
    'login': '/auth/login',
    'register': '/auth/register',
    'profile': '/secure/profile',
    'google':'/auth/google', 
    'facebook':'/auth/facebook'
}

export const authApis = () => {
    console.info(cookies.load('token'))
    return axios.create({
        baseURL: "http://localhost:8080/springserver/api/",
        headers: {
            'Authorization': `Bearer ${cookies.load('token')}`
        }
    })
}

export default axios.create({
    baseURL: "http://localhost:8080/springserver/api/"
})