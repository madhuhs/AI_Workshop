# AI-Assisted Debugging — Expected vs Actual Results

**Program:** `CartBuggy.java`  
**Workshop method:** Understand requirement → Run → Compare → Diagnose → Fix one bug → Run again.

## Bug 1 — Incorrect Subtotal Calculation

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

**Root cause:** `subtotal += item.price() + item.quantity();` uses addition instead of multiplication.

**Minimal fix:** `subtotal += item.price() * item.quantity();`

## Bug 2 — Incorrect Discount Condition

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

**Root cause:** `>=` includes ₹5,000 even though the requirement says *greater than* ₹5,000.

**Minimal fix:** `int discount = subtotal > 5000 ? subtotal / 10 : 0;`

## Bug 3 — Incorrect Quantity Validation

**Requirement:** Quantity must be **greater than zero**.

> Test this after fixing Bugs 1 and 2.

| Parameter | Expected | Actual (Bug 3 remains) |
|---|---|---|
| Product | Pen | Pen |
| Price | ₹20 | ₹20 |
| Quantity | 0 | 0 |
| Validation condition | `quantity <= 0` | `quantity < 0` |
| **Validation result** | **Reject quantity** | **Accept quantity ❌** |
| **Output** | **`ERROR: invalid quantity`** | **`subtotal=0, discount=0, total=0` ❌** |

**Root cause:** `< 0` rejects negative numbers but allows zero.

**Minimal fix:** `if (item.quantity() <= 0) return "ERROR: invalid quantity";`

## Debugging Progress — Actual Final Totals

| Test case | Initial execution | After fixing Bug 1 | After fixing Bug 2 | After fixing Bug 3 |
|---|---|---|---|---|
| Pen ₹20 × 3 | ❌ ₹23 | ✅ ₹60 | ✅ ₹60 | ✅ ₹60 |
| Bag ₹5,000 × 1 | ❌ ₹4,501 | ❌ ₹4,500 | ✅ ₹5,000 | ✅ ₹5,000 |
| Pen ₹20 × 0 | ❌ ₹20 | ❌ ₹0 | ❌ ₹0 | ✅ `ERROR: invalid quantity` |

### Exact console outputs at each stage

**Initial execution — all three bugs present**

```text
subtotal=23, discount=0, total=23
subtotal=5001, discount=500, total=4501
subtotal=20, discount=0, total=20
```

**After fixing Bug 1 only**

```text
subtotal=60, discount=0, total=60
subtotal=5000, discount=500, total=4500
subtotal=0, discount=0, total=0
```

**After fixing Bugs 1 and 2**

```text
subtotal=60, discount=0, total=60
subtotal=5000, discount=0, total=5000
subtotal=0, discount=0, total=0
```

**After fixing all three bugs**

```text
subtotal=60, discount=0, total=60
subtotal=5000, discount=0, total=5000
ERROR: invalid quantity
```

## Trainer Takeaway

**Requirement → Expected output → Actual output → Root cause → Smallest fix → Re-execute → Verify.**

A successful compilation does not prove the business logic is correct. Fix and verify **one defect at a time**. Passing these three cases does not replace broader regression testing.
