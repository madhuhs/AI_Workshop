### Reusable prompt formula

**EXPECTED + ACTUAL + FILE + SCOPE + VERIFICATION**

### Master debugging prompt — copy/paste

```text
Act as a careful [LANGUAGE] debugger. Inspect [FILE].
Expected: [EXPECTED]. Actual: [ACTUAL].
Find the root cause for THIS failure only. Explain it in 2 sentences,
then propose the smallest code change. Do not fix unrelated issues,
refactor, or edit other files. Give one command/test to verify the fix.
Wait for my approval before editing.
```

**Follow-up to apply an approved fix:**

```text
Apply only the proposed fix for this failure in [FILE].
Show the changed lines; do not alter anything else.
```

**Follow-up after manual testing:**

```text
The result is now [ACTUAL]. Expected [EXPECTED].
Recheck only this case; suggest the next minimal fix if needed.
```
---

## Challenge 1 — Shopping Cart Calculator

### Business requirements

1. Each cart item has a nonblank `name`, integer `price >= 0`, integer `quantity > 0`.
2. `subtotal = sum(price * quantity)`.
3. Apply `floor(subtotal * 10 / 100)` discount **only if subtotal > 5000**.
4. `total = subtotal - discount`.
5. Invalid item → error; empty cart → all zeros.

### Step-by-step defect investigation

| Step | Test input | Expected | What this isolates |
|---|---|---|---|
| C1 | Pen: price 20, quantity 3 | subtotal 60; discount 0; total 60 | Wrong line-total calculation |
| C2 | Bag: price 5000, quantity 1 | subtotal 5000; discount 0; total 5000 | Threshold boundary (run **after fixing C1**) |
| C3 | Pen: price 20, quantity 0 | Error | Invalid quantity accepted |

**Run order:** C1 → fix → rerun C1 → C2 → fix → rerun C2 → C3 → fix → rerun C3. Then run all three again.


**Requirement:** `subtotal = SUM(price × quantity)`.

| Parameter | Expected | Actual (buggy code) |
|---|---|---|
| Product | Pen | Pen |
| Price | ₹20 | ₹20 |
| Quantity | 3 | 3 |
| Calculation | `20 × 3` | `20 + 3` |
| **Subtotal** | **₹60** | **₹23 ❌** |
| Discount | ₹0 | ₹0 |
| **Final total** | **₹60** | **₹23 ❌** |

### Prompt C1 — Calculation

```text
Debug [FILE] in [LANGUAGE]. For [{name:"Pen",price:20,quantity:3}],
expected subtotal=60; actual result is wrong.
Identify the root cause and propose only the smallest fix.
Do not edit until I approve. Include one verification test.
```

### Prompt C2 — Boundary condition

```text
Debug [FILE]. A cart with one item priced 5000, quantity 1,
should receive no discount, but it does.
Find the boundary error only. Propose a minimal fix and test
for subtotals 5000 and 5001. Wait before editing.
```

### Prompt C3 — Validation

```text
Debug [FILE]. An item with quantity 0 must return an error,
but the program accepts it. Identify and fix only this validation rule.
Show the changed line and a test for quantities 0 and 1.
Wait for approval before editing.
```

### Manual verification checklist

| Input | Expected |
|---|---|
| `[{name:"Pen",price:20,quantity:3}]` | `{subtotal:60,discount:0,total:60}` |
| `[{name:"Bag",price:5000,quantity:1}]` | `{subtotal:5000,discount:0,total:5000}` |
| `[{name:"Bag",price:5001,quantity:1}]` | `{subtotal:5001,discount:500,total:4501}` |
| `[{name:"Pen",price:20,quantity:0}]` | Error |
| `[]` | `{subtotal:0,discount:0,total:0}` |

**Trainer checkpoint:** Why does fixing arithmetic not automatically fix discount eligibility or quantity validation?

---

## Challenge 2 — Customer Order Processor (Intermediate)

**Timebox:** 10 minutes  
**Files:** `java/OrdersBuggy.java`, `python/orders_buggy.py`, `javascript/orders_buggy.js`

### Business requirements

1. Analyze only orders with status `COMPLETED`.
2. Each order contains `id`, `customer`, `amount` (nonnegative integer), and `status`.
3. `spending[customer]` is the **sum** of that customer's completed order amounts.
4. `ordersAboveThreshold` contains IDs of completed orders with `amount > threshold` (strictly greater).
5. `topCustomer` is the customer with highest total spending. **On ties, choose the alphabetically earlier name.** Empty completed orders → `null` top customer.
6. Keep original order of IDs in `ordersAboveThreshold`.

### Shared demo data

```json
[
  {"id":1,"customer":"Zara","amount":100,"status":"COMPLETED"},
  {"id":2,"customer":"Zara","amount":200,"status":"COMPLETED"},
  {"id":3,"customer":"Asha","amount":300,"status":"COMPLETED"},
  {"id":4,"customer":"Ravi","amount":100,"status":"CANCELLED"}
]
```

**Threshold:** `200`

**Expected after all fixes:**

```json
{
  "spending": {"Zara":300,"Asha":300},
  "topCustomer": "Asha",
  "ordersAboveThreshold": [3]
}
```

### Step-by-step defect investigation

| Step | Expected | What this isolates |
|---|---|---|
| O1 | Zara total = 300 (100 + 200) | Overwriting instead of accumulating |
| O2 | At threshold 200, order 2 must be excluded; order 3 included | Inclusive vs exclusive filtering |
| O3 | With Zara=300 and Asha=300, topCustomer = `Asha` | Tie-breaking logic |

**Run order:** O1 → fix → rerun → O2 → fix → rerun → O3 → fix → rerun; finish with complete sample.

**Requirement:** Apply a 10% discount **only when subtotal > ₹5,000**.

> Test this after fixing Bug 1, so the subtotal is calculated correctly.

| Parameter | Expected | Actual (Bug 2 remains) |
|---|---|---|
| Product | Bag | Bag |
| Price | ₹5,000 | ₹5,000 |
| Quantity | 1 | 1 |
| Subtotal | ₹5,000 | ₹5,000 |
| Discount condition | `subtotal > 5000` | `subtotal >= 5000` |
| **Discount** | **₹0** | **₹500 ❌** |
| **Final total** | **₹5,000** | **₹4,500 ❌** |



### Prompt O1 — Aggregation

```text
Debug [FILE] in [LANGUAGE]. Zara has completed orders of 100 and 200,
so her spending must be 300, but it is not.
Explain the root cause; propose only the smallest change.
Wait before editing and give one verification case.
```

### Prompt O2 — Filtering

```text
Debug [FILE]. With threshold 200, only completed orders with
amount > 200 should be listed. An order of exactly 200 appears.
Propose the minimal condition change; do not touch aggregation.
Wait before editing.
```

### Prompt O3 — Tie-breaking

```text
Debug [FILE]. Zara and Asha both spent 300. topCustomer must be
Asha (alphabetically first), but the program selects Zara.
Fix only the tie-break logic. Preserve other behavior and
show a test with reversed customer input order. Wait before editing.
```

### Manual verification checklist

| Scenario | Expected |
|---|---|
| Zara orders 100 and 200 | `spending.Zara = 300` |
| Completed order amount exactly 200; threshold 200 | ID excluded |
| Completed order amount 300; threshold 200 | ID included |
| Zara=300, Asha=300 | `topCustomer = "Asha"` |
| Only cancelled orders | Empty spending, `topCustomer = null`, empty IDs |

**Trainer checkpoint:** Why does a correct total not guarantee a correct top-customer selection?

---

## Commands to run manually

Run these commands from the lab folder. Java examples require **JDK 17+** (records); JavaScript requires Node.js; Python requires Python 3.

| Language | Challenge 1 | Challenge 2 |
|---|---|---|
| Java | `cd java && javac CartBuggy.java && java CartBuggy` | `cd java && javac OrdersBuggy.java && java OrdersBuggy` |
| Python | `python python/cart_buggy.py` | `python python/orders_buggy.py` |
| JavaScript | `node javascript/cart_buggy.js` | `node javascript/orders_buggy.js` |

The demo files print sample results. **To verify additional cases**, edit the `cases`/`orders` test inputs or use your IDE's scratch runner. Don't rely only on the three initial demo outputs.

