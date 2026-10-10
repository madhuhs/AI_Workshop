def calculate(items):
    if not isinstance(items, list):
        raise ValueError("items must be a list")
    subtotal = 0
    for item in items:
        if not isinstance(item, dict):
            raise ValueError("invalid item")
        name, price, quantity = item.get("name"), item.get("price"), item.get("quantity")
        if (not isinstance(name, str) or not name.strip()
                or type(price) is not int or price < 0
                or type(quantity) is not int or quantity <= 0):
            raise ValueError("invalid item")
        subtotal += price * quantity
    discount = subtotal // 10 if subtotal > 5000 else 0
    return {"subtotal": subtotal, "discount": discount, "total": subtotal - discount}


if __name__ == "__main__":
    print(calculate([{"name": "Bag", "price": 3000, "quantity": 2}]))
