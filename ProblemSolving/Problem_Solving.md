
## Problem 1 — Shopping Bill Calculator (Beginner)

**Timebox:** 5 minutes  
**Goal:** Practice basic input validation and arithmetic.

### Problem statement

A shopkeeper needs to calculate the bill for a single product.

**Rules**
1. Input: product name (`string`), unit price (`integer rupees`), and quantity (`integer`).
2. Product name must not be blank; price must be >= 0; quantity must be > 0.
3. `total = price * quantity`.
4. On invalid input, return an error; do not calculate a bill.
5. Output a JSON-compatible object with `product`, `price`, `quantity`, and `total`, or `{ "error": "..." }`.

### Optimized prompt — copy/paste

```text
Act as a senior [LANGUAGE] developer. Build a single-file Shopping Bill Calculator.
Input: product (nonblank string), price (integer >= 0), quantity (integer > 0).
Output: {product, price, quantity, total}, where total = price * quantity; invalid input -> {error: message}.
Use only standard libraries. Include a runnable demo for the cases below and a brief run command. Keep logic separate from demo code; no frameworks or interactive input.
Cases: ("Pen", 20, 3) -> total 60; ("Book", 0, 2) -> total 0; ("Pen", 20, 0) -> error; ("", 20, 2) -> error.
```

### Manual verification

| Input | Expected result |
|---|---|
| `("Pen", 20, 3)` | `total = 60` |
| `("Book", 0, 2)` | `total = 0` |
| `("Pen", 20, 0)` | Error |
| `("", 20, 2)` | Error |
| `("Pen", -1, 2)` | Error |

**Trainer checkpoint:** Ask, “Why is quantity zero invalid while price zero is allowed?”

---

## Problem 2 — Shopping Cart Discount Calculator (Intermediate)

**Timebox:** 7 minutes  
**Goal:** Translate business rules and boundary conditions into code.

### Problem statement

A store wants to calculate the payable amount for a cart containing multiple products.

**Rules**
1. Input: list of items, each with `name`, `price` (integer rupees >= 0), `quantity` (integer > 0).
2. Each name must be nonblank; reject invalid items.
3. `subtotal = sum(price * quantity)`.
4. If **subtotal > 5000**, apply a 10% discount; otherwise discount is 0.
5. Discount is `floor(subtotal * 10 / 100)` in whole rupees.
6. `total = subtotal - discount`.
7. Empty cart is valid: all amounts are 0.
8. Output `{subtotal, discount, total}` or `{error: message}`.

### Optimized prompt — copy/paste

```text
Act as a senior [LANGUAGE] developer. Create a single-file Shopping Cart Calculator.
Input: list of {name, price, quantity}; name nonblank, price integer >= 0, quantity integer > 0.
subtotal = sum(price * quantity). If subtotal > 5000, discount = floor(subtotal * 10 / 100); else 0. total = subtotal - discount.
Return {subtotal, discount, total}; invalid item -> {error: message}; empty cart -> all zeros.
Use standard libraries only. Separate logic from a runnable demo, include the test cases below and a short run command. No frameworks or interactive input.
Tests: [{name:"Laptop",price:4000,quantity:1},{name:"Mouse",price:1000,quantity:2}] -> {subtotal:6000,discount:600,total:5400}; subtotal 5000 -> no discount; subtotal 5001 -> discount 500; [] -> zeros.
```

### Manual verification

| Cart | Expected result |
|---|---|
| Laptop ₹4,000 × 1 + Mouse ₹1,000 × 2 | Subtotal 6,000; discount 600; total 5,400 |
| Item ₹5,000 × 1 | Subtotal 5,000; discount 0; total 5,000 |
| Item ₹5,001 × 1 | Subtotal 5,001; discount 500; total 4,501 |
| Empty cart | Subtotal 0; discount 0; total 0 |
| Any item with quantity 0 | Error |

**Trainer checkpoint:** Ask, “What happens at exactly ₹5,000? Why?”

---

## Problem 3 — Customer Order Analysis (Intermediate)

**Timebox:** 8 minutes  
**Goal:** Use data structures, grouping, filtering, and deterministic tie-breaking.

### Problem statement

A small business wants a summary of customer spending from completed orders.

**Rules**
1. Input: list of orders `{customer, amount}` where customer is a nonblank string and amount is an integer >= 0.
2. Aggregate amounts per customer; customer names are case-sensitive and matched exactly.
3. Return `totals` keyed by customer, `topCustomer`, and `ordersAboveThreshold`.
4. An order qualifies for `ordersAboveThreshold` only when `amount > threshold` (strictly greater).
5. Threshold is an integer >= 0; reject invalid inputs.
6. For equal top totals, choose the customer whose name comes first alphabetically (case-sensitive lexicographic order).
7. Empty orders -> `totals = {}`, `topCustomer = null`, `ordersAboveThreshold = []`.
8. Preserve original order in `ordersAboveThreshold`.

### Optimized prompt — copy/paste

```text
Act as a senior [LANGUAGE] developer. Build a single-file Customer Order Analyzer.
Input: orders [{customer, amount}] and threshold; customer nonblank, amount and threshold integers >= 0.
Return {totals, topCustomer, ordersAboveThreshold}: sum spending per exact customer name; topCustomer has highest total (alphabetical name wins ties); include original orders with amount > threshold, preserving order.
Empty orders -> {{}, null, []}. Invalid input -> {error: message}.
Use only standard libraries; keep logic separate from runnable demo; provide a short run command. No frameworks or interactive input.
Test: orders [{customer:"Asha",amount:1500},{customer:"Ravi",amount:3000},{customer:"Asha",amount:2000}], threshold 2000 -> totals {Asha:3500,Ravi:3000}, topCustomer "Asha", above-threshold only Ravi's 3000 order.
```

### Manual verification

| Scenario | Expected result |
|---|---|
| Asha: 1,500 + 2,000; Ravi: 3,000; threshold 2,000 | `totals={Asha:3500,Ravi:3000}`, `topCustomer="Asha"`, above threshold = Ravi ₹3,000 |
| Asha ₹1,000; Ravi ₹1,000 | `topCustomer="Asha"` (tie-break) |
| One order amount = threshold | Excluded from `ordersAboveThreshold` |
| No orders | Empty totals, `topCustomer=null`, empty filtered list |
| Negative order amount | Error |

**Trainer checkpoint:** Ask, “Why must we define how ties are handled before generating code?”

---

## Universal follow-up prompts (short and reusable)

**Before coding — clarify:**
```text
List any missing or ambiguous rules in this requirement. Ask only essential questions; do not code yet.
```

**If a test fails — targeted correction:**
```text
Expected: [EXPECTED]. Actual: [ACTUAL]. Inspect the relevant logic, explain the cause in 2 lines, and propose the smallest fix. Do not change unrelated code.
```

**Review generated solution:**
```text
Check this code against every stated rule and boundary case. List mismatches only, with a test input for each.
```

**Convert into a teaching activity:**
```text
Turn this problem into a 5-minute beginner exercise: student instructions, 2 hints, and a separate trainer answer key. Use simple English.
```

