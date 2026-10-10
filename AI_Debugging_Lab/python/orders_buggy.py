import json

def analyze_orders(orders, threshold):
    if type(threshold) is not int or threshold < 0:
        return {"error": "invalid threshold"}
    spending = {}
    above_threshold = []
    for order in orders:
        customer = order["customer"]
        amount = order["amount"]
        status = order["status"]
        if status != "COMPLETED":
            continue
        spending[customer] = amount  # BUG O1: overwrite, should accumulate
        if amount >= threshold:  # BUG O2: threshold is exclusive
            above_threshold.append(order["id"])
    top_customer = max(spending, key=spending.get) if spending else None  # BUG O3: ties must use alphabetic order
    return {"spending": spending, "topCustomer": top_customer, "ordersAboveThreshold": above_threshold}

if __name__ == "__main__":
    orders = [
        {"id": 1, "customer": "Zara", "amount": 100, "status": "COMPLETED"},
        {"id": 2, "customer": "Zara", "amount": 200, "status": "COMPLETED"},
        {"id": 3, "customer": "Asha", "amount": 300, "status": "COMPLETED"},
        {"id": 4, "customer": "Ravi", "amount": 100, "status": "CANCELLED"},
    ]
    print(json.dumps(analyze_orders(orders, 200), sort_keys=True))
