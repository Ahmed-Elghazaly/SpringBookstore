import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { customerApi } from '../api/client';
import toast from 'react-hot-toast';

export default function Login() {
    const [custId, setCustId] = useState('');
    const [isRegistering, setIsRegistering] = useState(false);
    const [formData, setFormData] = useState({ username: '', password: '', email: '', shippingAddress: '' });
    const navigate = useNavigate();

    const handleLogin = (e) => {
        e.preventDefault();
        if (!custId) return;
        localStorage.setItem('customerId', custId);
        toast.success(`Welcome back, User #${custId}`);
        navigate('/');
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const res = await customerApi.register(formData);
            localStorage.setItem('customerId', res.data.customerId);
            toast.success("Account created!");
            navigate('/');
        } catch (err) {
            toast.error("Registration failed");
        }
    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50">
            <div className="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md border border-gray-100">
                <div className="text-center mb-8">
                    <h1 className="text-3xl font-bold text-brand-600">GoldenBooks</h1>
                    <p className="text-gray-500 mt-2">Database Systems Project 2025</p>
                </div>

                {!isRegistering ? (
                    <form onSubmit={handleLogin} className="space-y-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">Enter Customer ID</label>
                            <input
                                type="number"
                                value={custId}
                                onChange={(e) => setCustId(e.target.value)}
                                className="w-full px-4 py-3 rounded-lg border border-gray-300 focus:ring-2 focus:ring-brand-500 focus:border-brand-500 outline-none transition-all"
                                placeholder="e.g. 1"
                                required
                            />
                            <p className="text-xs text-gray-400 mt-2">Use '1' if you seeded the DB, or register below.</p>
                        </div>
                        <button type="submit" className="w-full bg-brand-600 text-white py-3 rounded-lg font-bold hover:bg-brand-700 transition-colors">
                            Access Store
                        </button>
                        <div className="text-center mt-4">
                            <button type="button" onClick={() => setIsRegistering(true)} className="text-sm text-brand-600 hover:underline">
                                New user? Register here
                            </button>
                        </div>
                    </form>
                ) : (
                    <form onSubmit={handleRegister} className="space-y-4">
                        <input className="w-full p-2 border rounded" placeholder="Username" onChange={e => setFormData({...formData, username: e.target.value})} required />
                        <input className="w-full p-2 border rounded" type="password" placeholder="Password" onChange={e => setFormData({...formData, password: e.target.value})} required />
                        <input className="w-full p-2 border rounded" type="email" placeholder="Email" onChange={e => setFormData({...formData, email: e.target.value})} required />
                        <input className="w-full p-2 border rounded" placeholder="Address" onChange={e => setFormData({...formData, shippingAddress: e.target.value})} required />

                        <button type="submit" className="w-full bg-green-600 text-white py-3 rounded-lg font-bold">Register</button>
                        <button type="button" onClick={() => setIsRegistering(false)} className="w-full text-gray-500 text-sm">Back to Login</button>
                    </form>
                )}
            </div>
        </div>
    );
}