import { useEffect, useState } from 'react';
import { orderApi } from '../api/client';

export default function OrderHistory() {
    const [orders, setOrders] = useState([]);
    const customerId = localStorage.getItem('customerId');

    useEffect(() => {
        orderApi.getHistory(customerId).then(res => setOrders(res.data));
    }, []);

    return (
        <div className="max-w-5xl mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-8">Order History</h1>
            <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                <table className="w-full text-left">
                    <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                        <th className="p-4 font-semibold text-gray-600">Date</th>
                        <th className="p-4 font-semibold text-gray-600">Order ID</th>
                        <th className="p-4 font-semibold text-gray-600">Book</th>
                        <th className="p-4 font-semibold text-gray-600">Qty</th>
                        <th className="p-4 font-semibold text-gray-600">Price</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-100">
                    {orders.map((row, i) => (
                        <tr key={i} className="hover:bg-gray-50">
                            <td className="p-4 text-gray-500">{row.orderDate}</td>
                            <td className="p-4 font-mono text-brand-600">#{row.orderId}</td>
                            <td className="p-4">
                                <div className="font-bold text-gray-900">{row.title}</div>
                                <div className="text-xs text-gray-400">{row.isbn}</div>
                            </td>
                            <td className="p-4">{row.quantity}</td>
                            <td className="p-4 font-medium text-green-600">${row.priceAtPurchase}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}