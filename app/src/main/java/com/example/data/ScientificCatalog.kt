package com.example.data

object ScientificCatalog {

    val calculusSymbols: List<ScientificSymbol> = listOf(
        ScientificSymbol(
            id = "calc_int",
            label = "∫",
            unicode = "∫",
            latex = "\\int",
            name = "Integral",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∮",
            shiftedUnicode = "∮",
            shiftedLatex = "\\oint",
            variants = listOf(
                SymbolVariant("∬", "∬", "\\iint", "Double integral"),
                SymbolVariant("∭", "∭", "\\iiint", "Triple integral"),
                SymbolVariant("∮", "∮", "\\oint", "Contour integral"),
                SymbolVariant("∯", "∯", "\\oiint", "Closed surface integral"),
                SymbolVariant("∰", "∰", "\\oiiint", "Closed volume integral"),
                SymbolVariant("∫ₐᵇ", "∫ₐᵇ ", "\\int_{a}^{b} ", "Definite integral")
            )
        ),
        ScientificSymbol(
            id = "calc_iint",
            label = "∬",
            unicode = "∬",
            latex = "\\iint",
            name = "Double Integral",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∯",
            shiftedUnicode = "∯",
            shiftedLatex = "\\oiint",
            variants = listOf(
                SymbolVariant("∭", "∭", "\\iiint", "Triple integral"),
                SymbolVariant("∯", "∯", "\\oiint", "Surface integral"),
                SymbolVariant("∰", "∰", "\\oiiint", "Volume integral")
            )
        ),
        ScientificSymbol(
            id = "calc_oint",
            label = "∮",
            unicode = "∮",
            latex = "\\oint",
            name = "Contour Integral",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∰",
            shiftedUnicode = "∰",
            shiftedLatex = "\\oiiint",
            variants = listOf(
                SymbolVariant("∯", "∯", "\\oiint", "Surface contour"),
                SymbolVariant("∰", "∰", "\\oiiint", "Volume contour"),
                SymbolVariant("∲", "∲", "\\ointclockwise", "Clockwise contour")
            )
        ),
        ScientificSymbol(
            id = "calc_partial",
            label = "∂",
            unicode = "∂",
            latex = "\\partial",
            name = "Partial Derivative",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∂/∂x",
            shiftedUnicode = "∂/∂x",
            shiftedLatex = "\\frac{\\partial}{\\partial x}",
            variants = listOf(
                SymbolVariant("∂/∂x", "∂/∂x", "\\frac{\\partial}{\\partial x}", "Partial w.r.t. x"),
                SymbolVariant("∂/∂t", "∂/∂t", "\\frac{\\partial}{\\partial t}", "Partial w.r.t. t"),
                SymbolVariant("∂²/∂x²", "∂²/∂x²", "\\frac{\\partial^2}{\\partial x^2}", "Second partial"),
                SymbolVariant("∂f/∂x", "∂f/∂x", "\\frac{\\partial f}{\\partial x}", "Partial of f")
            )
        ),
        ScientificSymbol(
            id = "calc_nabla",
            label = "∇",
            unicode = "∇",
            latex = "\\nabla",
            name = "Nabla / Gradient",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∇²",
            shiftedUnicode = "∇²",
            shiftedLatex = "\\nabla^2",
            variants = listOf(
                SymbolVariant("∇·", "∇·", "\\nabla \\cdot ", "Divergence"),
                SymbolVariant("∇×", "∇×", "\\nabla \\times ", "Curl"),
                SymbolVariant("∇²", "∇²", "\\nabla^2", "Laplacian"),
                SymbolVariant("□", "□", "\\Box", "d'Alembertian")
            )
        ),
        ScientificSymbol(
            id = "calc_delta",
            label = "Δ",
            unicode = "Δ",
            latex = "\\Delta",
            name = "Finite Difference / Laplacian",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "δ",
            shiftedUnicode = "δ",
            shiftedLatex = "\\delta",
            variants = listOf(
                SymbolVariant("Δx", "Δx", "\\Delta x", "Delta x"),
                SymbolVariant("Δt", "Δt", "\\Delta t", "Delta t"),
                SymbolVariant("δ", "δ", "\\delta", "Variational delta")
            )
        ),
        ScientificSymbol(
            id = "calc_sum",
            label = "∑",
            unicode = "∑",
            latex = "\\sum",
            name = "Summation",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∏",
            shiftedUnicode = "∏",
            shiftedLatex = "\\prod",
            variants = listOf(
                SymbolVariant("∑ₙ₌₀^∞", "∑ₙ₌₀^∞ ", "\\sum_{n=0}^{\\infty} ", "Infinite series"),
                SymbolVariant("∑ᵢ₌₁ⁿ", "∑ᵢ₌₁ⁿ ", "\\sum_{i=1}^{n} ", "Finite sum"),
                SymbolVariant("∏", "∏", "\\prod", "Product"),
                SymbolVariant("∐", "∐", "\\coprod", "Coproduct")
            )
        ),
        ScientificSymbol(
            id = "calc_prod",
            label = "∏",
            unicode = "∏",
            latex = "\\prod",
            name = "Product Operator",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∐",
            shiftedUnicode = "∐",
            shiftedLatex = "\\coprod",
            variants = listOf(
                SymbolVariant("∏ᵢ₌₁ⁿ", "∏ᵢ₌₁ⁿ ", "\\prod_{i=1}^{n} ", "Finite product"),
                SymbolVariant("∐", "∐", "\\coprod", "Coproduct")
            )
        ),
        ScientificSymbol(
            id = "calc_inf",
            label = "∞",
            unicode = "∞",
            latex = "\\infty",
            name = "Infinity",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "-∞",
            shiftedUnicode = "-∞",
            shiftedLatex = "-\\infty",
            variants = listOf(
                SymbolVariant("+∞", "+∞", "+\\infty", "Positive infinity"),
                SymbolVariant("-∞", "-∞", "-\\infty", "Negative infinity")
            )
        ),
        ScientificSymbol(
            id = "calc_lim",
            label = "lim",
            unicode = "lim",
            latex = "\\lim",
            name = "Limit",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "sup",
            shiftedUnicode = "sup",
            shiftedLatex = "\\sup",
            variants = listOf(
                SymbolVariant("lim x→∞", "lim_{x→∞} ", "\\lim_{x \\to \\infty} ", "Limit to infinity"),
                SymbolVariant("lim x→0", "lim_{x→0} ", "\\lim_{x \\to 0} ", "Limit to zero"),
                SymbolVariant("lim sup", "lim sup ", "\\limsup ", "Limit superior"),
                SymbolVariant("lim inf", "lim inf ", "\\liminf ", "Limit inferior")
            )
        ),
        ScientificSymbol(
            id = "calc_arrow",
            label = "→",
            unicode = "→",
            latex = "\\to",
            name = "Approaches / Maps To",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "↦",
            shiftedUnicode = "↦",
            shiftedLatex = "\\mapsto",
            variants = listOf(
                SymbolVariant("↦", "↦", "\\mapsto", "Maps to"),
                SymbolVariant("←", "←", "\\leftarrow", "Left arrow"),
                SymbolVariant("↔", "↔", "\\leftrightarrow", "Bi-directional"),
                SymbolVariant("⇒", "⇒", "\\Rightarrow", "Implies"),
                SymbolVariant("⇔", "⇔", "\\Leftrightarrow", "If and only if")
            )
        ),
        ScientificSymbol(
            id = "calc_ddx",
            label = "d/dx",
            unicode = "d/dx",
            latex = "\\frac{d}{dx}",
            name = "Derivative w.r.t. x",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "d²/dx²",
            shiftedUnicode = "d²/dx²",
            shiftedLatex = "\\frac{d^2}{dx^2}",
            variants = listOf(
                SymbolVariant("d/dt", "d/dt", "\\frac{d}{dt}", "Time derivative"),
                SymbolVariant("dy/dx", "dy/dx", "\\frac{dy}{dx}", "Leibniz derivative"),
                SymbolVariant("d²/dx²", "d²/dx²", "\\frac{d^2}{dx^2}", "Second derivative"),
                SymbolVariant("dⁿ/dxⁿ", "dⁿ/dxⁿ", "\\frac{d^n}{dx^n}", "nth derivative")
            )
        ),
        ScientificSymbol(
            id = "calc_pdx",
            label = "∂/∂x",
            unicode = "∂/∂x",
            latex = "\\frac{\\partial}{\\partial x}",
            name = "Partial Operator x",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∂/∂t",
            shiftedUnicode = "∂/∂t",
            shiftedLatex = "\\frac{\\partial}{\\partial t}",
            variants = listOf(
                SymbolVariant("∂/∂y", "∂/∂y", "\\frac{\\partial}{\\partial y}", "Partial y"),
                SymbolVariant("∂/∂z", "∂/∂z", "\\frac{\\partial}{\\partial z}", "Partial z"),
                SymbolVariant("∂/∂t", "∂/∂t", "\\frac{\\partial}{\\partial t}", "Partial t")
            )
        ),
        ScientificSymbol(
            id = "calc_prime",
            label = "′",
            unicode = "′",
            latex = "'",
            name = "First Derivative Prime",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "″",
            shiftedUnicode = "″",
            shiftedLatex = "''",
            variants = listOf(
                SymbolVariant("″", "″", "''", "Second derivative"),
                SymbolVariant("‴", "‴", "'''", "Third derivative"),
                SymbolVariant("˙", "̇", "\\dot{}", "Newton time dot")
            )
        ),
        ScientificSymbol(
            id = "calc_dx",
            label = "dx",
            unicode = "dx",
            latex = "\\,dx",
            name = "Differential dx",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "dt",
            shiftedUnicode = "dt",
            shiftedLatex = "\\,dt",
            variants = listOf(
                SymbolVariant("dy", "dy", "\\,dy", "Differential dy"),
                SymbolVariant("dz", "dz", "\\,dz", "Differential dz"),
                SymbolVariant("dt", "dt", "\\,dt", "Differential dt"),
                SymbolVariant("du", "du", "\\,du", "Differential du"),
                SymbolVariant("dV", "dV", "\\,dV", "Volume element"),
                SymbolVariant("dS", "dS", "\\,dS", "Surface element")
            )
        ),
        ScientificSymbol(
            id = "calc_div",
            label = "∇·",
            unicode = "∇·",
            latex = "\\nabla \\cdot ",
            name = "Divergence",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∇×",
            shiftedUnicode = "∇×",
            shiftedLatex = "\\nabla \\times "
        ),
        ScientificSymbol(
            id = "calc_curl",
            label = "∇×",
            unicode = "∇×",
            latex = "\\nabla \\times ",
            name = "Curl",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∇·",
            shiftedUnicode = "∇·",
            shiftedLatex = "\\nabla \\cdot "
        ),
        ScientificSymbol(
            id = "calc_laplacian",
            label = "∇²",
            unicode = "∇²",
            latex = "\\nabla^2",
            name = "Laplacian",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "□",
            shiftedUnicode = "□",
            shiftedLatex = "\\Box"
        ),
        ScientificSymbol(
            id = "calc_laplace_t",
            label = "ℒ",
            unicode = "ℒ",
            latex = "\\mathcal{L}",
            name = "Laplace Transform",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "ℱ",
            shiftedUnicode = "ℱ",
            shiftedLatex = "\\mathcal{F}",
            variants = listOf(
                SymbolVariant("ℒ⁻¹", "ℒ⁻¹", "\\mathcal{L}^{-1}", "Inverse Laplace"),
                SymbolVariant("ℱ", "ℱ", "\\mathcal{F}", "Fourier transform"),
                SymbolVariant("ℱ⁻¹", "ℱ⁻¹", "\\mathcal{F}^{-1}", "Inverse Fourier")
            )
        ),
        ScientificSymbol(
            id = "calc_fourier_t",
            label = "ℱ",
            unicode = "ℱ",
            latex = "\\mathcal{F}",
            name = "Fourier Transform",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "ℋ",
            shiftedUnicode = "ℋ",
            shiftedLatex = "\\mathcal{H}"
        ),
        ScientificSymbol(
            id = "calc_comp",
            label = "∘",
            unicode = "∘",
            latex = "\\circ",
            name = "Function Composition",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∗",
            shiftedUnicode = "∗",
            shiftedLatex = "\\ast"
        ),
        ScientificSymbol(
            id = "calc_tensor",
            label = "⊗",
            unicode = "⊗",
            latex = "\\otimes",
            name = "Tensor Product",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "⊕",
            shiftedUnicode = "⊕",
            shiftedLatex = "\\oplus",
            variants = listOf(
                SymbolVariant("⊕", "⊕", "\\oplus", "Direct sum"),
                SymbolVariant("⊙", "⊙", "\\odot", "Hadamard product")
            )
        ),
        ScientificSymbol(
            id = "calc_sqrt",
            label = "√( )",
            unicode = "√",
            latex = "\\sqrt{}",
            name = "Square Root (full radicand)",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∛",
            shiftedUnicode = "∛",
            shiftedLatex = "\\sqrt[3]{}",
            variants = listOf(
                SymbolVariant("∛", "∛", "\\sqrt[3]{}", "Cube root"),
                SymbolVariant("∜", "∜", "\\sqrt[4]{}", "Fourth root")
            )
        ),
        ScientificSymbol(
            id = "calc_frac",
            label = "a⁄b",
            unicode = "a⁄b",
            latex = "\\frac{}{}",
            name = "Fraction Template (top line bottom)",
            deck = KeyDeck.CALCULUS
        ),
        ScientificSymbol(
            id = "calc_nth_root",
            label = "ˣ√",
            unicode = "ˣ√",
            latex = "\\sqrt[n]{}",
            name = "Nth Root (full radicand)",
            deck = KeyDeck.CALCULUS
        ),
        ScientificSymbol(
            id = "calc_pm",
            label = "±",
            unicode = "±",
            latex = "\\pm",
            name = "Plus-Minus",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∓",
            shiftedUnicode = "∓",
            shiftedLatex = "\\mp"
        ),
        ScientificSymbol(
            id = "calc_approx",
            label = "≈",
            unicode = "≈",
            latex = "\\approx",
            name = "Approximately Equal",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "∼",
            shiftedUnicode = "∼",
            shiftedLatex = "\\sim",
            variants = listOf(
                SymbolVariant("≡", "≡", "\\equiv", "Identical"),
                SymbolVariant("≅", "≅", "\\cong", "Congruent"),
                SymbolVariant("∝", "∝", "\\propto", "Proportional")
            )
        ),
        ScientificSymbol(
            id = "calc_neq",
            label = "≠",
            unicode = "≠",
            latex = "\\neq",
            name = "Not Equal",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "≡",
            shiftedUnicode = "≡",
            shiftedLatex = "\\equiv"
        ),
        ScientificSymbol(
            id = "calc_leq",
            label = "≤",
            unicode = "≤",
            latex = "\\leq",
            name = "Less Than or Equal",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "≪",
            shiftedUnicode = "≪",
            shiftedLatex = "\\ll",
            variants = listOf(
                SymbolVariant("≪", "≪", "\\ll", "Much less than"),
                SymbolVariant("≲", "≲", "\\lesssim", "Less or similar")
            )
        ),
        ScientificSymbol(
            id = "calc_geq",
            label = "≥",
            unicode = "≥",
            latex = "\\geq",
            name = "Greater Than or Equal",
            deck = KeyDeck.CALCULUS,
            shiftedLabel = "≫",
            shiftedUnicode = "≫",
            shiftedLatex = "\\gg",
            variants = listOf(
                SymbolVariant("≫", "≫", "\\gg", "Much greater than"),
                SymbolVariant("≳", "≳", "\\gtrsim", "Greater or similar")
            )
        )
    )

    val constantSymbols: List<ScientificSymbol> = listOf(
        ScientificSymbol(
            id = "const_pi",
            label = "π",
            unicode = "π",
            latex = "\\pi",
            name = "Pi (Archimedes' Constant)",
            deck = KeyDeck.CONSTANTS,
            numericValue = "3.14159265359",
            units = "dimensionless",
            variants = listOf(
                SymbolVariant("3.14159", "3.14159265", "3.14159265", "8 decimal digits"),
                SymbolVariant("2π", "2π", "2\\pi", "Full turn"),
                SymbolVariant("π/2", "π/2", "\\frac{\\pi}{2}", "Quarter turn")
            )
        ),
        ScientificSymbol(
            id = "const_e",
            label = "e",
            unicode = "e",
            latex = "e",
            name = "Euler's Number",
            deck = KeyDeck.CONSTANTS,
            numericValue = "2.71828182846",
            units = "dimensionless",
            variants = listOf(
                SymbolVariant("eˣ", "eˣ", "e^{x}", "Exponential"),
                SymbolVariant("eⁱπ", "eⁱπ", "e^{i\\pi}", "Euler term"),
                SymbolVariant("2.71828", "2.71828183", "2.71828183", "Value")
            )
        ),
        ScientificSymbol(
            id = "const_tau",
            label = "τ",
            unicode = "τ",
            latex = "\\tau",
            name = "Tau (2π)",
            deck = KeyDeck.CONSTANTS,
            numericValue = "6.28318530718",
            units = "rad"
        ),
        ScientificSymbol(
            id = "const_phi",
            label = "φ",
            unicode = "φ",
            latex = "\\varphi",
            name = "Golden Ratio",
            deck = KeyDeck.CONSTANTS,
            numericValue = "1.61803398875",
            units = "dimensionless"
        ),
        ScientificSymbol(
            id = "const_gamma",
            label = "γ",
            unicode = "γ",
            latex = "\\gamma",
            name = "Euler–Mascheroni Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "0.57721566490",
            units = "dimensionless"
        ),
        ScientificSymbol(
            id = "const_i",
            label = "i",
            unicode = "i",
            latex = "i",
            name = "Imaginary Unit (√-1)",
            deck = KeyDeck.CONSTANTS,
            numericValue = "√(-1)",
            units = "complex"
        ),
        ScientificSymbol(
            id = "const_c",
            label = "c",
            unicode = "c",
            latex = "c",
            name = "Speed of Light in Vacuum",
            deck = KeyDeck.CONSTANTS,
            numericValue = "299792458",
            units = "m/s"
        ),
        ScientificSymbol(
            id = "const_h",
            label = "h",
            unicode = "h",
            latex = "h",
            name = "Planck Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "6.62607015e-34",
            units = "J·s"
        ),
        ScientificSymbol(
            id = "const_hbar",
            label = "ℏ",
            unicode = "ℏ",
            latex = "\\hbar",
            name = "Reduced Planck Constant (h/2π)",
            deck = KeyDeck.CONSTANTS,
            numericValue = "1.054571817e-34",
            units = "J·s"
        ),
        ScientificSymbol(
            id = "const_G",
            label = "G",
            unicode = "G",
            latex = "G",
            name = "Newtonian Constant of Gravitation",
            deck = KeyDeck.CONSTANTS,
            numericValue = "6.67430e-11",
            units = "m³/(kg·s²)"
        ),
        ScientificSymbol(
            id = "const_kb",
            label = "k_B",
            unicode = "k_B",
            latex = "k_{\\mathrm{B}}",
            name = "Boltzmann Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "1.380649e-23",
            units = "J/K"
        ),
        ScientificSymbol(
            id = "const_na",
            label = "N_A",
            unicode = "N_A",
            latex = "N_{\\mathrm{A}}",
            name = "Avogadro Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "6.02214076e23",
            units = "mol⁻¹"
        ),
        ScientificSymbol(
            id = "const_R",
            label = "R",
            unicode = "R",
            latex = "R",
            name = "Molar Gas Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "8.314462618",
            units = "J/(mol·K)"
        ),
        ScientificSymbol(
            id = "const_eps0",
            label = "ε₀",
            unicode = "ε₀",
            latex = "\\varepsilon_0",
            name = "Vacuum Electric Permittivity",
            deck = KeyDeck.CONSTANTS,
            numericValue = "8.8541878188e-12",
            units = "F/m"
        ),
        ScientificSymbol(
            id = "const_mu0",
            label = "μ₀",
            unicode = "μ₀",
            latex = "\\mu_0",
            name = "Vacuum Magnetic Permeability",
            deck = KeyDeck.CONSTANTS,
            numericValue = "1.25663706127e-6",
            units = "N/A²"
        ),
        ScientificSymbol(
            id = "const_qe",
            label = "qₑ",
            unicode = "qₑ",
            latex = "e",
            name = "Elementary Charge",
            deck = KeyDeck.CONSTANTS,
            numericValue = "1.602176634e-19",
            units = "C"
        ),
        ScientificSymbol(
            id = "const_me",
            label = "mₑ",
            unicode = "mₑ",
            latex = "m_e",
            name = "Electron Rest Mass",
            deck = KeyDeck.CONSTANTS,
            numericValue = "9.1093837139e-31",
            units = "kg"
        ),
        ScientificSymbol(
            id = "const_mp",
            label = "mₚ",
            unicode = "mₚ",
            latex = "m_p",
            name = "Proton Rest Mass",
            deck = KeyDeck.CONSTANTS,
            numericValue = "1.67262192595e-27",
            units = "kg"
        ),
        ScientificSymbol(
            id = "const_alpha",
            label = "α",
            unicode = "α",
            latex = "\\alpha",
            name = "Fine-Structure Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "7.2973525643e-3",
            units = "≈ 1/137.036"
        ),
        ScientificSymbol(
            id = "const_sigma",
            label = "σ",
            unicode = "σ",
            latex = "\\sigma",
            name = "Stefan–Boltzmann Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "5.670374419e-8",
            units = "W/(m²·K⁴)"
        ),
        ScientificSymbol(
            id = "const_g0",
            label = "g₀",
            unicode = "g₀",
            latex = "g_0",
            name = "Standard Acceleration of Gravity",
            deck = KeyDeck.CONSTANTS,
            numericValue = "9.80665",
            units = "m/s²"
        ),
        ScientificSymbol(
            id = "const_rydberg",
            label = "R∞",
            unicode = "R∞",
            latex = "R_\\infty",
            name = "Rydberg Constant",
            deck = KeyDeck.CONSTANTS,
            numericValue = "10973731.568157",
            units = "m⁻¹"
        ),
        ScientificSymbol(
            id = "const_bohr",
            label = "a₀",
            unicode = "a₀",
            latex = "a_0",
            name = "Bohr Radius",
            deck = KeyDeck.CONSTANTS,
            numericValue = "5.29177210544e-11",
            units = "m"
        ),
        ScientificSymbol(
            id = "const_flux",
            label = "Φ₀",
            unicode = "Φ₀",
            latex = "\\Phi_0",
            name = "Magnetic Flux Quantum",
            deck = KeyDeck.CONSTANTS,
            numericValue = "2.067833848e-15",
            units = "Wb"
        )
    )

    val greekSymbols: List<ScientificSymbol> = listOf(
        greek("alpha", "α", "Α", "\\alpha", "\\Alpha", "Alpha"),
        greek("beta", "β", "Β", "\\beta", "\\Beta", "Beta"),
        greek("gamma", "γ", "Γ", "\\gamma", "\\Gamma", "Gamma"),
        greek("delta", "δ", "Δ", "\\delta", "\\Delta", "Delta"),
        greek(
            "epsilon", "ε", "Ε", "\\varepsilon", "E", "Epsilon",
            listOf(SymbolVariant("ϵ", "ϵ", "\\epsilon", "Lunate epsilon"))
        ),
        greek("zeta", "ζ", "Ζ", "\\zeta", "Z", "Zeta"),
        greek("eta", "η", "Η", "\\eta", "H", "Eta"),
        greek(
            "theta", "θ", "Θ", "\\theta", "\\Theta", "Theta",
            listOf(SymbolVariant("ϑ", "ϑ", "\\vartheta", "Script theta"))
        ),
        greek("iota", "ι", "Ι", "\\iota", "I", "Iota"),
        greek("kappa", "κ", "Κ", "\\kappa", "K", "Kappa"),
        greek("lambda", "λ", "Λ", "\\lambda", "\\Lambda", "Lambda"),
        greek("mu", "μ", "Μ", "\\mu", "M", "Mu"),
        greek("nu", "ν", "Ν", "\\nu", "N", "Nu"),
        greek("xi", "ξ", "Ξ", "\\xi", "\\Xi", "Xi"),
        greek("pi", "π", "Π", "\\pi", "\\Pi", "Pi"),
        greek(
            "rho", "ρ", "Ρ", "\\rho", "P", "Rho",
            listOf(SymbolVariant("ϱ", "ϱ", "\\varrho", "Variant rho"))
        ),
        greek("sigma", "σ", "Σ", "\\sigma", "\\Sigma", "Sigma"),
        greek("tau", "τ", "Τ", "\\tau", "T", "Tau"),
        greek("upsilon", "υ", "Υ", "\\upsilon", "\\Upsilon", "Upsilon"),
        greek(
            "phi", "φ", "Φ", "\\varphi", "\\Phi", "Phi",
            listOf(SymbolVariant("ϕ", "ϕ", "\\phi", "Straight phi"))
        ),
        greek("chi", "χ", "Χ", "\\chi", "X", "Chi"),
        greek("psi", "ψ", "Ψ", "\\psi", "\\Psi", "Psi"),
        greek("omega", "ω", "Ω", "\\omega", "\\Omega", "Omega"),
        ScientificSymbol(
            id = "greek_aleph",
            label = "ℵ",
            unicode = "ℵ",
            latex = "\\aleph",
            name = "Aleph (Cardinality)",
            deck = KeyDeck.GREEK,
            shiftedLabel = "ℶ",
            shiftedUnicode = "ℶ",
            shiftedLatex = "\\beth"
        )
    )

    val logicSymbols: List<ScientificSymbol> = listOf(
        sym("log_forall", "∀", "\\forall", "For All", KeyDeck.LOGIC),
        sym("log_exists", "∃", "\\exists", "There Exists", KeyDeck.LOGIC, "∄", "\\nexists"),
        sym("log_in", "∈", "\\in", "Element Of", KeyDeck.LOGIC, "∉", "\\notin"),
        sym("log_notin", "∉", "\\notin", "Not Element Of", KeyDeck.LOGIC),
        sym("log_subset", "⊂", "\\subset", "Proper Subset", KeyDeck.LOGIC, "⊆", "\\subseteq"),
        sym("log_subseteq", "⊆", "\\subseteq", "Subset or Equal", KeyDeck.LOGIC, "⊈", "\\nsubseteq"),
        sym("log_supset", "⊃", "\\supset", "Superset", KeyDeck.LOGIC, "⊇", "\\supseteq"),
        sym("log_cup", "∪", "\\cup", "Union", KeyDeck.LOGIC, "⋃", "\\bigcup"),
        sym("log_cap", "∩", "\\cap", "Intersection", KeyDeck.LOGIC, "⋂", "\\bigcap"),
        sym("log_empty", "∅", "\\emptyset", "Empty Set", KeyDeck.LOGIC),
        sym("log_setminus", "∖", "\\setminus", "Set Difference", KeyDeck.LOGIC),
        sym("log_implies", "⇒", "\\Rightarrow", "Implies", KeyDeck.LOGIC, "⇐", "\\Leftarrow"),
        sym("log_iff", "⇔", "\\Leftrightarrow", "If and Only If", KeyDeck.LOGIC),
        sym("log_not", "¬", "\\neg", "Logical Not", KeyDeck.LOGIC),
        sym("log_and", "∧", "\\wedge", "Logical And / Wedge", KeyDeck.LOGIC),
        sym("log_or", "∨", "\\vee", "Logical Or", KeyDeck.LOGIC),
        sym("log_xor", "⊕", "\\oplus", "Exclusive Or / Direct Sum", KeyDeck.LOGIC),
        sym("log_vdash", "⊢", "\\vdash", "Provable / Turnstile", KeyDeck.LOGIC, "⊨", "\\models"),
        sym("log_therefore", "∴", "\\therefore", "Therefore", KeyDeck.LOGIC, "∵", "\\because"),
        sym("log_perp", "⊥", "\\perp", "Perpendicular / Orthogonal", KeyDeck.LOGIC, "∥", "\\parallel"),
        sym("log_angle", "∠", "\\angle", "Angle", KeyDeck.LOGIC, "°", "^\\circ"),
        sym("log_reals", "ℝ", "\\mathbb{R}", "Real Numbers", KeyDeck.LOGIC, "ℝⁿ", "\\mathbb{R}^n"),
        sym("log_complex", "ℂ", "\\mathbb{C}", "Complex Numbers", KeyDeck.LOGIC),
        sym("log_integers", "ℤ", "\\mathbb{Z}", "Integers", KeyDeck.LOGIC),
        sym("log_naturals", "ℕ", "\\mathbb{N}", "Natural Numbers", KeyDeck.LOGIC),
        sym("log_rationals", "ℚ", "\\mathbb{Q}", "Rational Numbers", KeyDeck.LOGIC),
        sym("log_qed", "∎", "\\blacksquare", "Q.E.D. Halmos", KeyDeck.LOGIC),
        sym("log_propto", "∝", "\\propto", "Proportional To", KeyDeck.LOGIC)
    )

    val scriptSymbols: List<ScientificSymbol> = listOf(
        script("sup_0", "⁰", "₀", "^0", "_0", "Power / Subscript 0"),
        script("sup_1", "¹", "₁", "^1", "_1", "Power / Subscript 1"),
        script("sup_2", "²", "₂", "^2", "_2", "Squared / Subscript 2"),
        script("sup_3", "³", "₃", "^3", "_3", "Cubed / Subscript 3"),
        script("sup_4", "⁴", "₄", "^4", "_4", "Power / Subscript 4"),
        script("sup_5", "⁵", "₅", "^5", "_5", "Power / Subscript 5"),
        script("sup_6", "⁶", "₆", "^6", "_6", "Power / Subscript 6"),
        script("sup_7", "⁷", "₇", "^7", "_7", "Power / Subscript 7"),
        script("sup_8", "⁸", "₈", "^8", "_8", "Power / Subscript 8"),
        script("sup_9", "⁹", "₉", "^9", "_9", "Power / Subscript 9"),
        script("sup_n", "ⁿ", "ₙ", "^n", "_n", "Index n"),
        script("sup_i", "ⁱ", "ᵢ", "^i", "_i", "Index i"),
        script("sup_plus", "⁺", "₊", "^+", "_+", "Superscript / Subscript +"),
        script("sup_minus", "⁻", "₋", "^-", "_-", "Superscript / Subscript -"),
        sym("op_langle", "⟨", "\\langle ", "Left Bra / Angle Bracket", KeyDeck.SCRIPTS),
        sym("op_rangle", "⟩", "\\rangle ", "Right Ket / Angle Bracket", KeyDeck.SCRIPTS),
        sym("op_norm", "‖", "\\|", "Vector Norm", KeyDeck.SCRIPTS),
        sym("op_lceil", "⌈", "\\lceil ", "Ceiling Left", KeyDeck.SCRIPTS, "⌊", "\\lfloor "),
        sym("op_rceil", "⌉", "\\rceil ", "Ceiling Right", KeyDeck.SCRIPTS, "⌋", "\\rfloor "),
        sym("op_cdot", "·", "\\cdot ", "Dot Product", KeyDeck.SCRIPTS),
        sym("op_times", "×", "\\times ", "Cross Product / Multiply", KeyDeck.SCRIPTS),
        sym("op_div", "÷", "\\div ", "Division", KeyDeck.SCRIPTS),
        sym("op_lparen", "(", "(", "Left Parenthesis", KeyDeck.SCRIPTS, "[", "["),
        sym("op_rparen", ")", ")", "Right Parenthesis", KeyDeck.SCRIPTS, "]", "]"),
        sym("op_lbrace", "{", "\\{", "Left Brace", KeyDeck.SCRIPTS),
        sym("op_rbrace", "}", "\\}", "Right Brace", KeyDeck.SCRIPTS),
        sym("op_caret", "^", "^", "Exponent Caret", KeyDeck.SCRIPTS, "_", "_"),
        sym("op_eq", "=", "=", "Equals", KeyDeck.SCRIPTS, "+", "+")
    )

    val formulaTemplates: List<FormulaTemplate> = listOf(
        FormulaTemplate(
            id = "tpl_def_int",
            shortLabel = "∫ₐᵇ f(x)dx",
            title = "Definite Integral",
            unicode = "∫ₐᵇ f(x) dx",
            latex = "\\int_{a}^{b} f(x)\\,dx",
            domain = "Calculus"
        ),
        FormulaTemplate(
            id = "tpl_limit",
            shortLabel = "lim x→∞",
            title = "Limit at Infinity",
            unicode = "lim_{x→∞} f(x) = L",
            latex = "\\lim_{x \\to \\infty} f(x) = L",
            domain = "Calculus"
        ),
        FormulaTemplate(
            id = "tpl_partial",
            shortLabel = "∂f/∂x",
            title = "Partial Derivative",
            unicode = "∂f/∂x = lim_{h→0} (f(x+h,y) - f(x,y))/h",
            latex = "\\frac{\\partial f}{\\partial x} = \\lim_{h \\to 0} \\frac{f(x+h, y) - f(x, y)}{h}",
            domain = "Multivariable"
        ),
        FormulaTemplate(
            id = "tpl_taylor",
            shortLabel = "∑ Taylor",
            title = "Taylor Series",
            unicode = "∑ₙ₌₀^∞ f⁽ⁿ⁾(a)/n! · (x - a)ⁿ",
            latex = "\\sum_{n=0}^{\\infty} \\frac{f^{(n)}(a)}{n!}(x-a)^n",
            domain = "Series"
        ),
        FormulaTemplate(
            id = "tpl_stokes",
            shortLabel = "∮ Stokes",
            title = "Generalized Stokes' Theorem",
            unicode = "∬_Σ (∇×F)·dS = ∮_{∂Σ} F·dr",
            latex = "\\iint_{\\Sigma} (\\nabla \\times \\mathbf{F}) \\cdot d\\mathbf{S} = \\oint_{\\partial\\Sigma} \\mathbf{F} \\cdot d\\mathbf{r}",
            domain = "Vector Calculus"
        ),
        FormulaTemplate(
            id = "tpl_gaussian",
            shortLabel = "∫ e^(-x²)",
            title = "Gaussian Integral",
            unicode = "∫_{-∞}^{∞} e^{-x²} dx = √π",
            latex = "\\int_{-\\infty}^{\\infty} e^{-x^2}\\,dx = \\sqrt{\\pi}",
            domain = "Calculus"
        ),
        FormulaTemplate(
            id = "tpl_schrodinger",
            shortLabel = "iℏ ∂ψ/∂t",
            title = "Time-Dependent Schrödinger Equation",
            unicode = "iℏ ∂ψ/∂t = -(ℏ²/2m)∇²ψ + Vψ",
            latex = "i\\hbar \\frac{\\partial \\psi}{\\partial t} = -\\frac{\\hbar^2}{2m}\\nabla^2\\psi + V\\psi",
            domain = "Quantum Physics"
        ),
        FormulaTemplate(
            id = "tpl_euler",
            shortLabel = "eⁱπ + 1 = 0",
            title = "Euler's Identity",
            unicode = "e^{iπ} + 1 = 0",
            latex = "e^{i\\pi} + 1 = 0",
            domain = "Complex Analysis"
        )
    )

    val allBuiltInSymbols: List<ScientificSymbol> =
        calculusSymbols + constantSymbols + greekSymbols + logicSymbols + scriptSymbols

    private val byIdMap: Map<String, ScientificSymbol> = allBuiltInSymbols.associateBy { it.id }

    fun symbolsForDeck(deck: KeyDeck, customSymbols: List<ScientificSymbol>): List<ScientificSymbol> {
        val base = when (deck) {
            KeyDeck.CALCULUS -> calculusSymbols
            KeyDeck.CONSTANTS -> constantSymbols
            KeyDeck.GREEK -> greekSymbols
            KeyDeck.LOGIC -> logicSymbols
            KeyDeck.SCRIPTS -> scriptSymbols
            KeyDeck.QWERTY -> emptyList()
        }
        return base + customSymbols.filter { it.deck == deck }
    }

    fun resolvePinnedSymbols(
        pinnedIds: List<String>,
        customSymbols: List<ScientificSymbol>
    ): List<ScientificSymbol> {
        val customById = customSymbols.associateBy { it.id }
        return pinnedIds.mapNotNull { id -> customById[id] ?: byIdMap[id] }
    }

    fun formatInsertion(
        symbol: ScientificSymbol,
        settings: KeyboardLayoutSettings,
        shifted: Boolean = false
    ): String {
        val useShifted = shifted && symbol.shiftedUnicode != null
        val base = when {
            symbol.deck == KeyDeck.CONSTANTS && symbol.numericValue != null -> {
                val symPart = if (settings.outputMode == OutputMode.LATEX) symbol.latex else symbol.unicode
                when (settings.constantInsertMode) {
                    ConstantInsertMode.SYMBOL -> symPart
                    ConstantInsertMode.VALUE -> symbol.numericValue
                    ConstantInsertMode.BOTH -> "$symPart = ${symbol.numericValue}"
                }
            }
            settings.outputMode == OutputMode.LATEX -> {
                if (useShifted) (symbol.shiftedLatex ?: symbol.latex) else symbol.latex
            }
            else -> {
                if (useShifted) (symbol.shiftedUnicode ?: symbol.unicode) else symbol.unicode
            }
        }
        val needsSpace = settings.autoSpaceOperators &&
            (symbol.deck == KeyDeck.CALCULUS || symbol.deck == KeyDeck.LOGIC) &&
            !base.endsWith(" ")
        return if (needsSpace) "$base " else base
    }

    private fun greek(
        slug: String,
        lower: String,
        upper: String,
        lowerLatex: String,
        upperLatex: String,
        name: String,
        variants: List<SymbolVariant> = emptyList()
    ) = ScientificSymbol(
        id = "greek_$slug",
        label = lower,
        unicode = lower,
        latex = lowerLatex,
        name = "Greek $name",
        deck = KeyDeck.GREEK,
        shiftedLabel = upper,
        shiftedUnicode = upper,
        shiftedLatex = upperLatex,
        variants = variants
    )

    private fun script(
        id: String,
        sup: String,
        sub: String,
        supLatex: String,
        subLatex: String,
        name: String
    ) = ScientificSymbol(
        id = id,
        label = sup,
        unicode = sup,
        latex = supLatex,
        name = name,
        deck = KeyDeck.SCRIPTS,
        shiftedLabel = sub,
        shiftedUnicode = sub,
        shiftedLatex = subLatex
    )

    private fun sym(
        id: String,
        unicode: String,
        latex: String,
        name: String,
        deck: KeyDeck,
        shiftedUnicode: String? = null,
        shiftedLatex: String? = null
    ) = ScientificSymbol(
        id = id,
        label = unicode,
        unicode = unicode,
        latex = latex,
        name = name,
        deck = deck,
        shiftedLabel = shiftedUnicode,
        shiftedUnicode = shiftedUnicode,
        shiftedLatex = shiftedLatex
    )
}
