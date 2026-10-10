# Feature Development Lab — Starter Code

Choose **one** language. The three implementations intentionally start **without coupon support** and **with correct existing cart behavior**.

Run baseline:
- Java: `cd java && javac ShoppingCart.java && java ShoppingCart`
- Python: `cd python && python3 shopping_cart.py`
- JavaScript: `cd javascript && node shoppingCart.js`

All print subtotal 6000, discount 600, total 5400 (format differs).

Read `AI_Assisted_Feature_Development.md` for feature requirements, optimized prompt, acceptance tests, and instructor checkpoints.

Java: add a `calculate(List<Item> items, String coupon)` overload while preserving `calculate(List<Item> items)`; update result type to support the `discountSource` string (e.g., `Map<String,Object>`). Python/JavaScript: add an optional second parameter. No prebuilt solution is included: the feature is the live exercise.
