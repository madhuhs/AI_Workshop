function calculate(items) {
  if (!Array.isArray(items)) throw new Error('items must be an array');
  let subtotal = 0;
  for (const item of items) {
    if (!item || typeof item.name !== 'string' || !item.name.trim() ||
        !Number.isSafeInteger(item.price) || item.price < 0 ||
        !Number.isSafeInteger(item.quantity) || item.quantity <= 0) {
      throw new Error('invalid item');
    }
    subtotal += item.price * item.quantity;
    if (!Number.isSafeInteger(subtotal)) throw new Error('subtotal too large');
  }
  const discount = subtotal > 5000 ? Math.floor(subtotal / 10) : 0;
  return { subtotal, discount, total: subtotal - discount };
}
module.exports = { calculate };
if (require.main === module) console.log(calculate([{name:'Bag', price:3000, quantity:2}]));
