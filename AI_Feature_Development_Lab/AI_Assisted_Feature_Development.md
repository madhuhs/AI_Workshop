
### Reusable prompt formula

**FILE + EXISTING BEHAVIOR + NEW RULES + NON-REGRESSION + TESTS**

## Starting application — verified baseline

Each starter program has a `calculate(items)` function/method. Input items have `name`, `price` (integer rupees >= 0), and `quantity` (integer > 0). A nonblank name is required. `subtotal = sum(price * quantity)`. An automatic discount of `floor(subtotal * 10 / 100)` applies only when **subtotal > ₹5,000**. Otherwise discount is zero. `total = subtotal - discount`. Empty cart yields zeros; invalid input raises an error.

Baseline result for `[{name: "Bag", price: 3000, quantity: 2}]`: `{subtotal: 6000, discount: 600, total: 5400}`. **Run the starter before requesting any edits.**

## New feature — Coupon Code Support

1. Extend `calculate(items)` to accept an **optional coupon code**; calls without a coupon must keep working.
2. Recognized coupons: `SAVE10` = 10% off and `SAVE20` = 20% off.
3. Coupon is eligible only when **subtotal > ₹2,000** (₹2,000 is not eligible).
4. Coupon codes are **case-sensitive**; blank/unknown codes are invalid. Omitted coupon or `null`/`None` means no coupon.
5. Only one coupon per order. Input is one optional string, not a list; non-string values are invalid.
6. Coupon and existing automatic discounts **do not stack**. Apply whichever eligible discount is **larger**; on a tie, choose the automatic discount. Both discounts are computed on the original subtotal, rounded **down to whole rupees**.
7. Keep the existing output fields `{subtotal, discount, total}` and add `{discountSource}` with values `"automatic"`, `"coupon"`, or `"none"` for **all calls**.
8. Keep input validation and existing calculations unchanged. No frameworks, dependencies, UI, or network calls.

### Optimized feature prompt — copy/paste

```text
Act as a careful [LANGUAGE] developer. Update only [FILE].
Keep calculate(items) working; add optional coupon input.
SAVE10=10%, SAVE20=20%; eligible only if subtotal>2000.
Reject unknown/blank/non-string coupons; codes are case-sensitive.
Do not stack discounts: use larger of coupon and existing auto 10% (>5000);
on a tie prefer automatic. Floor discounts to integer rupees.
Return subtotal, discount, total, discountSource (automatic/coupon/none).
Preserve validation and old behavior; standard library only.
Show changed code and run command. Add tests for 2000, 2001, 5000,
5001, invalid coupon, and no coupon. Do not change unrelated files.
```

### Prompt 1 — Analyze before coding (2 min)

```text
Read [FILE] and the coupon requirements. Identify the smallest change,
possible ambiguities, and 5 essential tests. Do not edit yet.
```

### Prompt 2 — Implement (5 min)

Use the optimized feature prompt above. Review the diff before accepting.

### Prompt 3 — Verify and repair (5 min)

```text
For [FILE], compare these actual results [PASTE OUTPUT] with the
acceptance table. Fix only failing cases; preserve all passing cases.
Show the minimal diff and one command to rerun the checks.
```

## Acceptance tests — manual execution

For compactness, each case below uses **one item** with `name="Bag"`, `quantity=1`, and `price=subtotal` (unless specified). Expected objects contain all four output fields.

| # | Subtotal | Coupon | Expected discount | Expected total | Source | Why |
|---|---:|---|---:|---:|---|---|
| F1 | 2000 | SAVE10 | 0 | 2000 | none | strict coupon threshold |
| F2 | 2001 | SAVE10 | 200 | 1801 | coupon | integer flooring |
| F3 | 5000 | SAVE20 | 1000 | 4000 | coupon | auto not eligible at 5000 |
| F4 | 5001 | SAVE20 | 1000 | 4001 | coupon | coupon wins over auto |
| F5 | 6000 | SAVE10 | 600 | 5400 | automatic | tie prefers auto |
| F6 | 6000 | omitted | 600 | 5400 | automatic | regression |
| F7 | 1000 | omitted | 0 | 1000 | none | regression |
| F8 | 6000 | BAD | error | — | — | unknown coupon |
| F9 | 6000 | save10 | error | — | — | case sensitive |
| F10 | 6000 | "" | error | — | — | blank coupon |
| F11 | 6000 | 10 | error | — | — | non-string coupon |
| F12 | 0 | omitted; empty cart | 0 | 0 | none | regression |
| F13 | invalid quantity 0 | omitted | error | — | — | validation regression |
