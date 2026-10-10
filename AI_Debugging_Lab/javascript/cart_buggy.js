function calculateCart(items) {
  if (!Array.isArray(items)) return { error: 'items must be a list' };
  let subtotal = 0;
  for (const item of items) {
    if (typeof item?.name !== 'string' || !item.name.trim()) return { error: 'invalid name' };
    if (!Number.isInteger(item.price) || item.price < 0) return { error: 'invalid price' };
    if (!Number.isInteger(item.quantity) || item.quantity < 0) return { error: 'invalid quantity' }; // BUG C3
    subtotal += item.price + item.quantity; // BUG C1
  }
  const discount = subtotal >= 5000 ? Math.floor(subtotal / 10) : 0; // BUG C2
  return { subtotal, discount, total: subtotal - discount };
}
const cases = [
  [{ name: 'Pen', price: 20, quantity: 3 }],
  [{ name: 'Bag', price: 5000, quantity: 1 }],
  [{ name: 'Pen', price: 20, quantity: 0 }]
];
for (const c of cases) console.log(JSON.stringify(calculateCart(c)));
