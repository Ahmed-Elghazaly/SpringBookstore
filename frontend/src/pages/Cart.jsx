import { useEffect, useState } from 'react';
import { cartApi } from '../api/client';
import toast from 'react-hot-toast';
import { Trash2, CreditCard } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function Cart() {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const customerId = localStorage.getItem('customerId');
    const navigate = useNavigate();

    const fetchCart = async () => {
        try {
            const res = await cartApi.get(customerId);
            setCart(res.data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchCart(); }, []);

    const handleUpdate = async (isbn, qty) => {
        if (qty < 1) return;
        await cartApi.update(customerId, isbn, qty);
        fetchCart();
    };

    const handleRemove = async (isbn) => {
        await cartApi.remove(customerId, isbn);
        toast.success("Item removed");
        fetchCart();
    };

    const handleCheckout = async () => {
        try {
            const res = await cartApi.checkout(customerId);
            toast.success(`Order Placed! ID: ${res.data.orderId}`);
            navigate('/orders');
        } catch (err) {
            toast.error("Checkout failed. Check stock or balance.");
        }
    };

    if (loading) return <div className="p-10 text-center">Loading cart...</div>;
    if (!cart || cart.items.length === 0) return <div className="p-10 text-center text-gray-500">Your cart is empty.</div>;

    return (
        <div className="max-w-4xl mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-8">Shopping Cart</h1>
            <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                {cart.items.map((item) => (
                    <div key={item.isbn} className="flex items-center justify-between p-6 border-b border-gray-100 last:border-0">
                        <div>
                            <p className="text-xs text-gray-400 font-mono">ISBN: {item.isbn}</p>
                            <h3 className="font-bold text-lg text-gray-800">Book Title (Lookup needed in real app)</h3>
                        </div>
                        <div className="flex items-center gap-6">
                            <div className="flex items-center border rounded-lg">
                                <button onClick={() => handleUpdate(item.isbn, item.quantity - 1)} className="px-3 py-1 hover:bg-gray-100">-</button>
                                <span className="px-3 font-mono">{item.quantity}</span>
                                <button onClick={() => handleUpdate(item.isbn, item.quantity + 1)} className="px-3 py-1 hover:bg-gray-100">+</button>
                            </div>
                            <button onClick={() => handleRemove(item.isbn)} className="text-red-400 hover:text-red-600">
                                <Trash2 size={20} />
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            <div className="mt-8 flex justify-end">
                <button
                    onClick={handleCheckout}
                    className="bg-green-600 text-white px-8 py-4 rounded-xl font-bold text-lg hover:bg-green-700 flex items-center gap-3 shadow-lg shadow-green-200"
                >
                    <CreditCard /> Checkout Now
                </button>
            </div>
        </div>
    );
}