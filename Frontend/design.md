# Obsidian Pulse

## Essence
A premium, dark-mode-first design system that radiates confidence and technical sophistication. The aesthetic is defined by deep, near-black backgrounds punctuated by vivid magenta and emerald accents, creating a high-contrast, futuristic atmosphere. The system evokes authority, innovation, and exclusivity—designed for decision-makers who value measurable impact over empty promises.

## Color Palette

### Backgrounds
- Primary: `rgb(26, 22, 23)` / `#1a1617` — Main page background, warm near-black
- Secondary: `rgb(13, 13, 13)` / `#0d0d0d` — Alternate section background, pure near-black
- Surface: `rgba(18, 14, 16, 0.92)` — Card/panel surfaces, glass-morphism base
- Overlay: `rgba(26, 22, 23, 0.9)` — Navigation bar, semi-transparent dark

### Text
- Primary: `rgb(255, 255, 255)` / `#ffffff` — Headings, primary content
- Secondary: `rgba(255, 255, 255, 0.7)` — Body text, descriptions
- Muted: `rgb(166, 173, 187)` / `#a6adbb` — Tertiary text, labels

### Accents
- Brand Primary: `rgb(255, 0, 141)` / `#ff008d` — Hot pink/magenta, CTAs, highlights
- Brand Secondary: `rgb(35, 209, 139)` / `#23d18b` — Emerald green, success states, checkmarks
- Brand Primary Glow: `rgba(255, 0, 141, 0.1)` — Subtle pink background tint
- Brand Primary Border: `rgba(255, 0, 141, 0.04)` — Card inner glow border

### Semantic
- Success: `rgb(35, 209, 139)` / `#23d18b` — Positive indicators, checkmarks
- Warning: `rgb(255, 0, 141)` / `#ff008d` — Alert/warning indicators
- Borders: `rgba(255, 255, 255, 0.08)` — Subtle card borders
- Dividers: `rgba(255, 255, 255, 0.06)` — Navigation bottom border
- Hover Surface: `rgba(255, 255, 255, 0.05)` — Interactive hover states

## Typography

### Families
- Headings: `"Bebas Neue"`, fallback to system-ui, sans-serif — Bold, condensed display face
- Body: `"Inter"`, fallback to system-ui, -apple-system, sans-serif — Clean, modern sans-serif

### Scale
| Level | Size | Weight | Line Height | Usage |
|-------|------|--------|-------------|-------|
| H1 | 80px | 700 | 81.6px (1.02) | Hero titles |
| H2 | 60.8px | 400 | 66.88px (1.1) | Section headings |
| Body | 16px | 400 | 24px (1.5) | Paragraph text |
| Button | 14px | 700 | — | CTA buttons |
| Label | 14px | 700 | — | Section labels, tags |

### Treatments
- Headings use `"Bebas Neue"` with tight letter-spacing (`-1.5px` on H1)
- Button text is `uppercase` with `0.5px` letter-spacing
- Section labels/tags appear in uppercase with accent color
- Body text uses `rgba(255, 255, 255, 0.7)` for reduced emphasis

## Spacing

### Base Unit
Systematic spacing based on an 8px grid with common multipliers.

### Common Values
- xs: `8px` — Inline spacing, tight gaps
- sm: `16px` — Card padding, element gaps
- md: `28px` — Button horizontal padding
- lg: `48px` — Section vertical padding (compact)
- xl: `120px` — Section vertical padding (standard)
- 2xl: `140px` — Hero top padding

## Elevation

### Shadows
- Card: `rgba(0, 0, 0, 0.55) 0px 24px 60px 0px` — Deep, dramatic card shadow
- Glow: `rgba(255, 0, 141, 0.04) 0px 0px 0px 1px inset` — Subtle inner pink glow on cards

### Border Radii
- Small: `14px` — Cards, panels
- Large: `50px` — Buttons, pills

### Borders
- Card: `1px solid rgba(255, 255, 255, 0.08)` — Subtle glass-morphism edge
- Navigation: `1px solid rgba(255, 255, 255, 0.06)` — Very subtle divider
- Button: `2px solid rgb(255, 0, 141)` — Bold accent border

## Interactive States

### Buttons
- Default: Transparent background, `2px solid rgb(255, 0, 141)` border, magenta text, uppercase, `50px` border-radius
- Hover: Background fills with accent color, text inverts to white
- Active: Slight scale reduction for tactile feedback
- Transition: Smooth color/background transitions

### Links
- Default: White text, no underline
- Hover: Accent color highlight

### Cards
- Default: Glass-morphism surface with subtle border and deep shadow
- Hover: Elevated shadow, subtle border brightening

## Motion

### Principles
Smooth and purposeful. Animations serve to guide attention and provide feedback, never to distract. The overall motion language is restrained and professional—transitions are quick but not abrupt, creating a sense of precision and polish.

### Patterns
- Page transitions: Fade-in with subtle upward movement
- Hover states: 200-300ms ease transitions on color/background
- Scroll reveals: Elements fade in as they enter viewport
- Button interactions: Quick color fill on hover, subtle scale on press

## Design Principles
- **Dark-first authority**: The deep, near-black palette establishes premium positioning and reduces visual noise
- **Accent as signal**: Magenta and emerald are used sparingly and purposefully—only for CTAs, success states, and key highlights
- **Glass-morphism depth**: Semi-transparent surfaces with subtle borders create layered depth without heaviness
- **Typographic hierarchy through contrast**: Display headings (Bebas Neue) vs body text (Inter) creates clear visual hierarchy
- **Restraint over decoration**: Minimal ornamentation; the system relies on spacing, typography, and color contrast

## Implementation Notes
- The site uses Next.js with CSS custom properties for theming (primarily for third-party components like `iti` phone input)
- Font loading uses fallback fonts (`"Bebas Neue Fallback"`, `"Inter Fallback"`) for CLS prevention
- The glass-morphism effect combines `rgba` backgrounds, `backdrop-filter` (implied), subtle borders, and dramatic box-shadows
- Color values use modern CSS color functions (`lab()` color space) alongside traditional `rgb()`/`rgba()`
- The 50px border-radius on buttons creates a pill shape regardless of button size
- Section spacing is generous (120px vertical padding) creating breathing room between content blocks
