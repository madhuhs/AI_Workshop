function analyzeOrders(orders, threshold) {
  if (!Number.isInteger(threshold) || threshold < 0) return { error: 'invalid threshold' };
  const spending = {};
  const ordersAboveThreshold = [];
  for (const order of orders) {
    if (order.status !== 'COMPLETED') continue;
    spending[order.customer] = order.amount; // BUG O1
    if (order.amount >= threshold) ordersAboveThreshold.push(order.id); // BUG O2
  }
  const customers = Object.keys(spending);
  const topCustomer = customers.length ? customers.reduce((best, c) => spending[c] > spending[best] ? c : best) : null; // BUG O3
  return { spending, topCustomer, ordersAboveThreshold };
}
const orders = [
  { id: 1, customer: 'Zara', amount: 100, status: 'COMPLETED' },
  { id: 2, customer: 'Zara', amount: 200, status: 'COMPLETED' },
  { id: 3, customer: 'Asha', amount: 300, status: 'COMPLETED' },
  { id: 4, customer: 'Ravi', amount: 100, status: 'CANCELLED' }
];
console.log(JSON.stringify(analyzeOrders(orders, 200)));
