Repetitive steps have been abstracted into global preconditions, and scenarios are tiered from critical user paths (P0) to asynchronous edge cases (P2).

---

## Global Preconditions & Setup

| Parameter | Value |
|---|---|
| **Target Application** | Any online shopping platform (examples reference Periplus / Tokopedia conventions) |
| **Default User State** | Unless otherwise stated in a specific precondition column, the user is **authenticated** with a registered account |
| **Default Cart State** | Unless otherwise stated, the **cart is empty** at the start of each test case |
| **Default Stock State** | Unless otherwise stated, all target products have **available stock > 0** |
| **Execution Note** | *Actual Result* and *Status* fields are omitted from this document. Those fields are populated in the live execution log |
| **ID Convention** | Test IDs are permanent keys and are intentionally non-sequential across priority tiers. The suffix pattern (e.g., `002B`, `002C`) indicates a variant of the same base scenario — for example, all `002x` IDs are add-to-cart variants. |

---

## Test Coverage Summary

| Area | Scope |
|---|---|
| **Functional** | Add (logged-in & guest, single & bulk qty, duplicate, PDP qty selector), Remove (single, last item, undo), Update quantity, Multiple items, Partial selection subtotal, Double-click protection |
| **Validation** | Non-numeric, float, zero, negative, empty field, extremely large number, exact stock boundary, over-stock |
| **Business Rules** | Max 10 shops per cart, max 400 total items, shop slot freed on full removal, boundary happy paths |
| **Inventory Sync** | Product deleted, stock -> 0, partial stock drop, full restock, partial restock (fewer than cart qty), price change, flash sale expiry |
| **Pricing** | Discounted item subtotal, mixed discounted + full-price subtotal, coupon/promo code, free shipping threshold |
| **Persistence** | Logout -> re-login, guest browser close/reopen, cross-device (logged-in) |

## Extended Coverage Matrix

The following edge cases and validations have been identified and mapped for coverage. Each scenario is assigned a priority tier to communicate execution order and relative risk.

---

## P0 -- Core Critical Path

> These are the fundamental user journeys. If any P0 test fails, the cart feature is considered **fundamentally broken** and must be resolved before other testing continues.

| ID | Scenario | Specific Precondition | Expected Outcome |
|---|---|---|---|
| TC-CART-001 | Add standard item to cart | Cart is empty; product has stock > 0 | Item appears in cart; cart counter increments by 1; subtotal equals item price. |
| TC-CART-002B | Guest user adds item to cart | User is **not logged in** (guest session) | Item added successfully without requiring authentication; item displays correct title and price. |
| TC-CART-002 | Update quantity to a valid value | Cart has >=1 item; new quantity is within stock limit | Item's line total updates correctly (unit price x new qty); cart subtotal recalculates. |
| TC-CART-002C | Add item with custom quantity (>1) from the PDP | Product has >=5 units in stock | Item added with quantity 5; line total = unit price x 5; cart subtotal reflects full amount. |
| TC-CART-003 | Remove one item from a multi-item cart | Cart has >=2 items | Removed item disappears; subtotal decreases by that item's value; remaining items are unaffected. |
| TC-CART-003B | Remove the last remaining item | Cart has exactly 1 item | Cart displays an empty-state message (e.g., "Your cart is empty"); counter resets to 0; subtotal shows 0 or is hidden. |
| TC-CART-005 | Add a duplicate item already in the cart | Cart holds exactly 1 unit of Item A | Quantity for Item A increments to 2; no new duplicate row is created; subtotal doubles. |
| TC-CART-006 | Add a second, distinct item to the cart | Cart already holds 1 unit of Item A | Cart displays both items as separate rows; quantities and prices for each item are preserved; subtotal = sum of both. |
| TC-CART-023 | Subtotal reflects only selected (checked) items | Cart has >=2 items; both checkboxes selected by default | After unchecking one item, the subtotal updates to include only the checked item's price; unchecked item remains visible but excluded from total. |

---

## P1 -- Input Validations & Business Constraints

> This tier proves boundary awareness and understanding of business rules (e.g., Tokopedia-style shop/item limits).

| ID | Scenario | Test Action | Expected Outcome |
|---|---|---|---|
| TC-CART-002D | "Add to Cart" button is disabled for an out-of-stock product | Navigate to a product's PDP where stock = 0 | Button is visually disabled (greyed out) and not clickable; an "Out of Stock" label is shown; item cannot be added by any UI interaction. |
| TC-CART-002E | Pre-order item behaves like a standard item | Navigate to a pre-order product's PDP and click Add to Cart | Item added to cart normally; title, quantity, and price display correctly; subtotal updates; no blocking error shown. |
| TC-CART-002F | Double-clicking "Add to Cart" adds the item only once | Rapidly double-click the **Add to Cart** button | Item appears exactly once with quantity = 1; button is disabled or shows a loading state after the first click, preventing duplicate submission. |
| TC-CART-008 | Zero or negative quantity is rejected | Enter `0` or `-1` into the quantity field and click Update | Input rejected; quantity resets to 1 (or previous valid value); validation message shown (e.g., "Quantity must be at least 1"); subtotal not recalculated with invalid value. |
| TC-CART-009 | Non-numeric text input is rejected | Enter `abc` or `@#$` into the quantity field and click Update | Input rejected; quantity resets to previous valid integer; validation message shown; no system error. |
| TC-CART-009B | Float / decimal input is rejected | Enter `1.5` into the quantity field and click Update | Input rejected as an invalid non-integer; validation message shown (e.g., "Please enter a whole number"); quantity remains unchanged. |
| TC-CART-009C | Extremely large number is rejected | Enter `99999999999999999999` into the quantity field and click Update | Input rejected; validation message indicates limit reached (e.g., "Quantity limit reached"); no crash, integer overflow, or unexpected behaviour. |
| TC-CART-009D | Empty quantity field treated as removal | Clear the quantity field entirely and click Update | System interprets blank as 0; item is removed from cart (equivalent to qty = 0); subtotal updates; remaining items unaffected. |
| TC-CART-007 | Quantity exceeding available stock is rejected | Enter a value equal to (available stock + 1) into the quantity field and click Update | Input rejected; quantity capped at max available stock; validation message shown (e.g., "Only X units available"); subtotal uses valid quantity only. |
| TC-CART-009E | Quantity exactly equal to available stock is accepted (boundary) | Item has known stock of N units; enter exactly N in the quantity field | Input accepted; no validation error shown; line total updates correctly to unit price x N; subtotal reflects new amount. |
| TC-CART-010 | Checkout is blocked when the cart is empty | Navigate to the cart page with no items; click **Checkout** | Checkout does not proceed; warning message displayed (e.g., "Your cart is empty"). |
| TC-CART-011 | Adding an item from an 11th unique shop is blocked | Cart already contains items from exactly 10 distinct shops | Item not added; validation message shown (e.g., "Maximum number of shops reached"); cart contents and counter unchanged. |
| TC-CART-011C | Adding an item from a 10th unique shop succeeds (boundary) | Cart contains items from exactly 9 distinct shops | Item added successfully; no shop-limit error triggered; cart now shows items from 10 distinct shops. |
| TC-CART-011B | Removing all items from one shop frees the slot for a new shop | Cart has items from exactly 10 shops; remove all items from 1 shop | Cart now holds 9 distinct shops; adding an item from a previously blocked new shop succeeds; no shop-limit error shown. |
| TC-CART-012 | Adding from an existing shop is not blocked by the shop count | Cart holds 20 items all from Shop A | New item from the same Shop A added successfully; no shop-limit validation triggered (limit applies to distinct shops, not item count). |
| TC-CART-013 | Increasing a single item's quantity past 400 is blocked | Cart has 1 item with quantity = 400 (the total maximum) | Increasing quantity to 401 is rejected; validation message shown; total cart quantity remains 400. |
| TC-CART-014 | Increasing quantity past 400 across multiple items is blocked | Cart has Item A: qty 200 and Item B: qty 200 (total = 400) | Increasing either item's quantity by 1 is rejected; validation message shown; total cart quantity remains 400. |
| TC-CART-015 | Adding a new item when total quantity is at 400 is blocked | Cart contains a single item with quantity = 400 | New item is not added; warning message shown (e.g., "Maximum cart capacity reached"); total cart quantity remains 400. |

---

## P2 -- Asynchronous Sync & State Persistence

> This tier validates more complex concerns: data that changes underneath the user while they browse, and cart state that must survive across sessions and devices.

| ID | Scenario | Specific Precondition | Expected Outcome |
|---|---|---|---|
| TC-CART-016 | Deleted product is automatically removed from cart | Item in cart is deleted from the seller's catalog via admin/seller panel | On refresh, item no longer appears in cart; subtotal updates to exclude it; user may see a notification that an item was removed. |
| TC-CART-017 | Out-of-stock item moved to non-checkout section | Item in active cart has its stock set to 0 via admin/seller panel | On refresh, item moves to a visually separate "out of stock" section; excluded from subtotal; cannot be selected for checkout. |
| TC-CART-017B | Partial stock drop auto-adjusts cart quantity | Cart holds qty = 7 for an item; admin reduces stock to 5 (not 0) | Item remains in the active cart; quantity automatically adjusted down to 5; user notified of the adjustment; subtotal recalculated using qty = 5. |
| TC-CART-018 | Fully restocked item returns to the active cart | Item is in the out-of-stock section (stock = 0); admin restocks it to qty > 0 | On refresh, item moves back to the active cart section; becomes selectable; subtotal updates to include it again. |
| TC-CART-018B | Restock with fewer units than saved cart quantity caps the quantity | Cart holds a saved qty = 10 for an out-of-stock item (stock = 0); admin restocks to only 3 units | Item returns to active section with qty = 3 (capped at new stock), not the original 10; user notified of the adjustment; subtotal calculated using qty = 3. |
| TC-CART-018C | Price change while item is in cart is reflected on refresh | Item in cart has a known price (e.g., Rp 150,000); admin updates price to Rp 175,000 | On refresh, cart displays the new price (Rp 175,000); user notified that the price of one or more items has changed; subtotal recalculates using the new price. |
| TC-CART-019 | Discounted item uses discounted price in subtotal | A product has an active discount (e.g., 20% off, strikethrough shown) | Item displays the discounted price; subtotal increases by the discounted price, not the original; all totals correct. |
| TC-CART-019B | Mixed discounted and full-price items calculate the correct subtotal | Cart has Item A (discounted: Rp 80,000, was Rp 100,000) and Item B (full-price: Rp 50,000) | Subtotal = Rp 130,000 (80,000 + 50,000); Item A does not use its original price in any calculation; no mixing of original and discounted prices. |
| TC-CART-019C | Valid coupon / promo code reduces the cart subtotal | A valid active coupon code is available (e.g., SAVE10 for 10% off); cart has >=1 item | Coupon accepted; confirmation shown (e.g., "Coupon applied: 10% off"); cart subtotal reduces by the correct discount amount; applied coupon and discount value visibly shown in cart summary. |
| TC-CART-019D | Free shipping threshold indicator updates correctly | Free shipping threshold = Rp 200,000; cart subtotal is below threshold (e.g., Rp 150,000) | **Below threshold**: shows remaining amount needed (e.g., "Add Rp 50,000 more for free shipping"). **At or above threshold**: indicator confirms free shipping; shipping fee shown as Rp 0. |
| TC-CART-019E | Flash sale expiry reverts item to regular price | Item in cart was added during a flash sale (e.g., Rp 80,000 vs. regular Rp 120,000); sale expires while item is in cart | On refresh, item price updates to Rp 120,000; user notified that the price has changed (e.g., "The price of one item has changed"); subtotal recalculates at the regular price. |
| TC-CART-020 | Cart persists for a logged-in user after logout and re-login | Logged-in user has >=1 item in cart | After logging out and back in with the same credentials, cart contains the same items with original quantities intact. |
| TC-CART-021 | Guest cart persists after browser close and reopen | Guest (not logged in) user has >=1 item in cart | After fully closing and reopening the browser on the same device, cart items are still present (persisted via browser cookies or local storage); title, qty, and price display correctly. |
| TC-CART-022 | Logged-in cart synchronizes across different browsers or devices | User is logged in on Device A with >=1 item in cart | Logging in to the same account on Device B shows the identical cart state (same items, quantities, and subtotal); no items missing or duplicated. |
| TC-CART-003C | Undo item removal restores the item | Remove item from cart; an Undo toast/snackbar appears | Clicking **Undo** within the notification window restores the item with its original quantity and price; cart counter and subtotal revert to pre-removal values. |