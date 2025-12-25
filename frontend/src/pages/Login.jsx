import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {authApi, customerApi} from '../api/client';
import toast from 'react-hot-toast';
import {ShieldCheck, User} from 'lucide-react';

export default function Login() {
    const [role, setRole] = useState('CUSTOMER');
    const [isRegistering, setIsRegistering] = useState(false);

    const [loginData, setLoginData] = useState({username: '', password: ''});

    const [regData, setRegData] = useState({
        username: '', password: '', email: '', firstName: '', lastName: '', phoneNumber: '', shippingAddress: ''
    });

    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await authApi.login({...loginData, role});

            // FIX: Backend returns 'userId', not 'id'
            const userData = res.data;

            localStorage.setItem('user', JSON.stringify(userData));

            if (userData.role === 'CUSTOMER') {
                // FIX: Grab 'userId' from response
                localStorage.setItem('customerId', userData.userId);
            }

            toast.success(`Welcome ${userData.name}!`);

            if (userData.role === 'ADMIN') navigate('/admin');
            else navigate('/');

        } catch (err) {
            console.error(err);
            toast.error("Invalid Username or Password");
        }
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await customerApi.register(regData);
            toast.success("Account created! Please login.");
            setIsRegistering(false);
        } catch (err) {
            console.error(err);
            const msg = err.response?.data?.message || "Registration failed.";
            toast.error(msg);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50">
            <div className="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md border border-gray-100">
                <div className="text-center mb-6">
                    <h1 className="text-3xl font-bold text-brand-600">GoldenBooks</h1>
                    <p className="text-gray-500 text-sm">Database Systems Project 2025</p>
                </div>

                {!isRegistering && (
                    <div className="flex bg-gray-100 p-1 rounded-lg mb-6">
                        <button
                            onClick={() => setRole('CUSTOMER')}
                            className={`flex-1 flex items-center justify-center gap-2 py-2 rounded-md font-medium transition-all ${
                                role === 'CUSTOMER' ? 'bg-white text-brand-600 shadow-sm' : 'text-gray-500 hover:text-gray-700'
                            }`}
                        >
                            <User size={18}/> Customer
                        </button>
                        <button
                            onClick={() => setRole('ADMIN')}
                            className={`flex-1 flex items-center justify-center gap-2 py-2 rounded-md font-medium transition-all ${
                                role === 'ADMIN' ? 'bg-white text-red-600 shadow-sm' : 'text-gray-500 hover:text-gray-700'
                            }`}
                        >
                            <ShieldCheck size={18}/> Admin
                        </button>
                    </div>
                )}

                {!isRegistering ? (
                    <form onSubmit={handleLogin} className="space-y-4">
                        <input
                            className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                            placeholder={role === 'ADMIN' ? "Admin Username" : "Customer Username"}
                            value={loginData.username}
                            onChange={e => setLoginData({...loginData, username: e.target.value})}
                            required
                        />
                        <input
                            type="password"
                            className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                            placeholder="Password"
                            value={loginData.password}
                            onChange={e => setLoginData({...loginData, password: e.target.value})}
                            required
                        />
                        <button type="submit"
                                className={`w-full py-3 rounded-lg font-bold text-white transition-colors ${
                                    role === 'ADMIN' ? 'bg-red-600 hover:bg-red-700' : 'bg-brand-600 hover:bg-brand-700'
                                }`}>
                            Login as {role === 'ADMIN' ? 'Administrator' : 'Customer'}
                        </button>

                        {role === 'CUSTOMER' && (
                            <p className="text-center text-sm text-gray-500 mt-4">
                                No account? <button type="button" onClick={() => setIsRegistering(true)}
                                                    className="text-brand-600 font-bold hover:underline">Sign up</button>
                            </p>
                        )}
                    </form>
                ) : (
                    <form onSubmit={handleRegister} className="space-y-3">
                        <div className="flex gap-2">
                            <input className="w-1/2 p-2 border rounded" placeholder="First Name"
                                   onChange={e => setRegData({...regData, firstName: e.target.value})} required/>
                            <input className="w-1/2 p-2 border rounded" placeholder="Last Name"
                                   onChange={e => setRegData({...regData, lastName: e.target.value})} required/>
                        </div>
                        <input className="w-full p-2 border rounded" placeholder="Username"
                               onChange={e => setRegData({...regData, username: e.target.value})} required/>
                        <input className="w-full p-2 border rounded" type="email" placeholder="Email"
                               onChange={e => setRegData({...regData, email: e.target.value})} required/>
                        <input className="w-full p-2 border rounded" type="tel" placeholder="Phone"
                               onChange={e => setRegData({...regData, phoneNumber: e.target.value})} required/>
                        <input className="w-full p-2 border rounded" type="password" placeholder="Password"
                               onChange={e => setRegData({...regData, password: e.target.value})} required/>
                        <input className="w-full p-2 border rounded" placeholder="Shipping Address"
                               onChange={e => setRegData({...regData, shippingAddress: e.target.value})} required/>

                        <button type="submit"
                                className="w-full bg-green-600 text-white py-3 rounded-lg font-bold hover:bg-green-700">Register
                        </button>
                        <button type="button" onClick={() => setIsRegistering(false)}
                                className="w-full text-gray-500 text-sm">Back to Login
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
}