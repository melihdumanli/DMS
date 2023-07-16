import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import '../assets/styles/login.scss';
import "react-toastify/dist/ReactToastify.css";

const loginUrl = 'http://localhost:8080/api/v1/auth/login';
const registerUrl = 'http://localhost:8080/api/v1/auth/register';

const Login: React.FC = () => {
    const [firstname, setFirstName] = useState('')
    const [lastname, setLastName] = useState('')
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isLoginForm, setIsLoginForm] = useState(true)
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const notify = () => {
        toast.error(error, {
            position: toast.POSITION.TOP_RIGHT
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        isLoginForm
            ? axios.post(loginUrl, {
                email,
                password
            })
                .then(response => {
                    localStorage.setItem('access_token', response.data.access_token);
                    localStorage.setItem('refresh_token', response.data.refresh_token);
                    navigate('/home', { replace: true });
                })
                .catch(error => {
                    setError("Wrong email or password. Please try again.")
                    notify()
                })
            : axios.post(registerUrl, {
                firstname,
                lastname,
                email,
                password
            })
                .then(response => {
                    setIsLoginForm(true)
                })
                .catch(error => {
                    setError("Register request failed. Please try again.")
                    notify()
                })
    };
    const validateEmail = () => {
        const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
        if (!emailRegex.test(email)) {
            setError('Please enter a valid email address');
        }
    };

    return (
        <div className="container">
            <div className="login-container">
                <div className="header-container">
                    <div
                        className="header"
                        onClick={() => setIsLoginForm(true)}
                        style={{
                            backgroundColor: isLoginForm ? 'transparent' : '#fff',
                            borderTopRightRadius: 0,
                            color: isLoginForm ? '#fff' : '#333'
                        }}>Login</div>
                    <div
                        className="header"
                        onClick={() => setIsLoginForm(false)}
                        style={{
                            backgroundColor: isLoginForm ? '#fff' : 'transparent',
                            borderTopLeftRadius: 0,
                            color: isLoginForm ? '#333' : '#fff'
                        }}>Register</div>
                </div>
                <form className="login-form" onSubmit={handleSubmit}>
                    {isLoginForm && (
                        <div>
                            <div className="row">
                                <input
                                    type="text"
                                    placeholder="E-Mail"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    onBlur={() => validateEmail()}
                                />
                            </div>
                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                    )}
                    {!isLoginForm && (
                        <div>
                            <div className="row">
                                <input
                                    type="text"
                                    placeholder="First Name"
                                    value={firstname}
                                    onChange={(e) => setFirstName(e.target.value)}
                                />
                            </div>
                            <div className="row">
                                <input
                                    type="text"
                                    placeholder="Lastname"
                                    value={lastname}
                                    onChange={(e) => setLastName(e.target.value)}
                                />
                            </div>
                            <div className="row">
                                <input
                                    type="text"
                                    placeholder="E-Mail"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    onBlur={() => validateEmail()}
                                />
                            </div>
                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                    )}
                    <button type="submit">Submit</button>
                </form>
                <ToastContainer />
            </div>
        </div>
    );
};

export default Login;
