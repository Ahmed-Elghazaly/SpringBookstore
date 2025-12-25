import {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import toast from 'react-hot-toast';
import api from '../api/client';

export default function Profile() {
    const customerId = localStorage.getItem('customerId');
    const navigate = useNavigate();

    const [profile, setProfile] = useState({
        firstName: '', lastName: '', email: '', phoneNumber: '', shippingAddress: '', password: ''
    });

    useEffect(() => {
        // Fetch current profile
        api.get(`/customers/${customerId}`)
            .then(res => setProfile({
                ...res.data, password: '', phoneNumber: res.data.phoneNumber || ''  // Handle null
            }))
            .catch(err => console.error(err));
    }, []);

    const handleUpdate = async (e) => {
        e.preventDefault();
        try {
            await api.put(`/customers/${customerId}`, profile);
            toast.success("Profile updated successfully!");
        } catch (err) {
            toast.error("Failed to update profile");
        }
    };

    return (<div className="max-w-2xl mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-8">Edit Profile</h1>
            <form onSubmit={handleUpdate} className="bg-white p-8 rounded-xl shadow-sm border space-y-4">
                <div className="grid grid-cols-2 gap-4">
                    <input
                        type="text"
                        placeholder="First Name"
                        value={profile.firstName}
                        onChange={(e) => setProfile({...profile, firstName: e.target.value})}
                        className="p-3 border rounded-lg"
                    />
                    <input
                        type="text"
                        placeholder="Last Name"
                        value={profile.lastName}
                        onChange={(e) => setProfile({...profile, lastName: e.target.value})}
                        className="p-3 border rounded-lg"
                    />
                </div>
                <input
                    type="email"
                    placeholder="Email"
                    value={profile.email}
                    onChange={(e) => setProfile({...profile, email: e.target.value})}
                    className="w-full p-3 border rounded-lg"
                />
                <input
                    type="text"
                    placeholder="Phone Number"
                    value={profile.phoneNumber}
                    onChange={(e) => setProfile({...profile, phoneNumber: e.target.value})}
                    className="w-full p-3 border rounded-lg"
                />
                <input
                    type="text"
                    placeholder="Shipping Address"
                    value={profile.shippingAddress}
                    onChange={(e) => setProfile({...profile, shippingAddress: e.target.value})}
                    className="w-full p-3 border rounded-lg"
                />
                <input
                    type="password"
                    placeholder="New Password (leave blank to keep current)"
                    value={profile.password}
                    onChange={(e) => setProfile({...profile, password: e.target.value})}
                    className="w-full p-3 border rounded-lg"
                />
                <button type="submit"
                        className="w-full bg-brand-600 text-white py-3 rounded-lg font-bold hover:bg-brand-700">
                    Update Profile
                </button>
            </form>
        </div>);
}