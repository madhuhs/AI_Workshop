import json

def calculate_cart(items):
    if not isinstance(items, list):
        return {"error": "items must be a list"}
    subtotal = 0
    for item in items:
        if not isinstance(item.get("name"), str) or not item["name"].strip():
            return {"error": "invalid name"}
        if type(item.get("price")) is not int or item["price"] < 0:
            return {"error": "invalid price"}
        if type(item.get("quantity")) is not int or item["quantity"] < 0:  # BUG C3
            return {"error": "invalid quantity"}
        subtotal += item["price"] + item["quantity"]  # BUG C1
    discount = subtotal // 10 if subtotal >= 5000 else 0  # BUG C2
    return {"subtotal": subtotal, "discount": discount, "total": subtotal - discount}

if __name__ == "__main__":
    cases = [
        [{"name": "Pen", "price": 20, "quantity": 3}],
        [{"name": "Bag", "price": 5000, "quantity": 1}],
        [{"name": "Pen", "price": 20, "quantity": 0}],
    ]
    for case in cases:
        print(json.dumps(calculate_cart(case)))
