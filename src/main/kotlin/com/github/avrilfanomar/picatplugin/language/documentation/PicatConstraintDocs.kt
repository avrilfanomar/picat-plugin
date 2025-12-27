package com.github.avrilfanomar.picatplugin.language.documentation

/**
 * Documentation entry for a Picat predicate/function.
 *
 * @property name The name of the predicate/function.
 * @property signature The signature including arity (e.g., "fd_dom(FDVar)/1").
 * @property module The module this belongs to (cp, sat, mip, smt).
 * @property category The documentation category.
 * @property description Human-readable description.
 * @property parameters List of parameter descriptions.
 * @property examples Usage examples.
 * @property seeAlso Related predicates/functions.
 */
data class PicatDocEntry(
    val name: String,
    val signature: String,
    val module: String,
    val category: DocCategory,
    val description: String,
    val parameters: List<ParamDoc> = emptyList(),
    val examples: List<String> = emptyList(),
    val seeAlso: List<String> = emptyList()
)

/**
 * Parameter documentation.
 *
 * @property name The parameter name.
 * @property description Description of the parameter.
 */
data class ParamDoc(
    val name: String,
    val description: String
)

/**
 * Categories of constraint-related documentation.
 */
enum class DocCategory {
    /** Domain variable functions like fd_dom, fd_min, etc. */
    DOMAIN_FUNCTION,
    /** Table constraints like table_in, table_notin. */
    TABLE_CONSTRAINT,
    /** Global constraints like all_different, circuit, etc. */
    GLOBAL_CONSTRAINT,
    /** Solver invocation functions like solve, solve_all. */
    SOLVER_FUNCTION,
    /** Arithmetic functions usable in constraints like sum, count. */
    ARITHMETIC_FUNCTION,
    /** Solver options like ff, ffc, $min, etc. */
    SOLVER_OPTION
}

/**
 * Registry of documentation for Picat constraint predicates and functions.
 * Based on the official Picat documentation (constraints.tex).
 */
@Suppress("LargeClass")
object PicatConstraintDocs {

    private val entries: Map<String, List<PicatDocEntry>> by lazy { buildDocRegistry() }

    /**
     * Get documentation for a symbol by name and optional arity.
     */
    fun getDocumentation(name: String, arity: Int? = null): PicatDocEntry? {
        val candidates = entries[name] ?: return null
        return if (arity != null) {
            candidates.find { extractArity(it.signature) == arity } ?: candidates.firstOrNull()
        } else {
            candidates.firstOrNull()
        }
    }

    /**
     * Get all documentation entries.
     */
    fun getAllEntries(): List<PicatDocEntry> = entries.values.flatten()

    private fun extractArity(signature: String): Int? {
        val match = Regex("/(\\d+)").find(signature)
        return match?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun buildDocRegistry(): Map<String, List<PicatDocEntry>> {
        val docs = mutableListOf<PicatDocEntry>()

        docs += domainFunctions()
        docs += tableConstraints()
        docs += arithmeticFunctions()
        docs += globalConstraints()
        docs += solverFunctions()
        docs += solverOptions()

        return docs.groupBy { it.name }
    }

    @Suppress("LongMethod")
    private fun domainFunctions(): List<PicatDocEntry> = listOf(
        PicatDocEntry(
            name = "fd_dom",
            signature = "fd_dom(FDVar)/1",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Returns the domain of FDVar as a list of integers. " +
                "If FDVar is an integer, the returned list contains just that integer.",
            parameters = listOf(ParamDoc("FDVar", "A finite domain variable or integer")),
            examples = listOf("X :: 1..5, Dom = fd_dom(X)  % Dom = [1,2,3,4,5]")
        ),
        PicatDocEntry(
            name = "fd_min",
            signature = "fd_min(FDVar)/1",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Returns the minimum value (lower bound) of the domain of FDVar.",
            parameters = listOf(ParamDoc("FDVar", "A finite domain variable")),
            examples = listOf("X :: 5..10, Min = fd_min(X)  % Min = 5")
        ),
        PicatDocEntry(
            name = "fd_max",
            signature = "fd_max(FDVar)/1",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Returns the maximum value (upper bound) of the domain of FDVar.",
            parameters = listOf(ParamDoc("FDVar", "A finite domain variable")),
            examples = listOf("X :: 5..10, Max = fd_max(X)  % Max = 10")
        ),
        PicatDocEntry(
            name = "fd_size",
            signature = "fd_size(FDVar)/1",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Returns the size (number of values) of the domain of FDVar.",
            parameters = listOf(ParamDoc("FDVar", "A finite domain variable")),
            examples = listOf("X :: 1..10, Size = fd_size(X)  % Size = 10")
        ),
        PicatDocEntry(
            name = "fd_min_max",
            signature = "fd_min_max(FDVar, Min, Max)/3",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Binds Min to the lower bound and Max to the upper bound of FDVar's domain.",
            parameters = listOf(
                ParamDoc("FDVar", "A finite domain variable"),
                ParamDoc("Min", "Output: the minimum value"),
                ParamDoc("Max", "Output: the maximum value")
            )
        ),
        PicatDocEntry(
            name = "fd_degree",
            signature = "fd_degree(FDVar)/1",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Returns the number of propagators attached to FDVar. Only available in the cp module.",
            parameters = listOf(ParamDoc("FDVar", "A finite domain variable"))
        ),
        PicatDocEntry(
            name = "fd_next",
            signature = "fd_next(FDVar, Elm)/2",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Returns the next element after Elm in FDVar's domain. " +
                "Throws an exception if Elm has no next element.",
            parameters = listOf(
                ParamDoc("FDVar", "A finite domain variable"),
                ParamDoc("Elm", "An element in the domain")
            )
        ),
        PicatDocEntry(
            name = "fd_prev",
            signature = "fd_prev(FDVar, Elm)/2",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Returns the previous element before Elm in FDVar's domain. " +
                "Throws an exception if Elm has no previous element.",
            parameters = listOf(
                ParamDoc("FDVar", "A finite domain variable"),
                ParamDoc("Elm", "An element in the domain")
            )
        ),
        PicatDocEntry(
            name = "fd_true",
            signature = "fd_true(FDVar, Elm)/2",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "True if the integer Elm is an element in the domain of FDVar.",
            parameters = listOf(
                ParamDoc("FDVar", "A finite domain variable"),
                ParamDoc("Elm", "An integer to check")
            )
        ),
        PicatDocEntry(
            name = "fd_false",
            signature = "fd_false(FDVar, Elm)/2",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "True if the integer Elm is NOT an element in the domain of FDVar.",
            parameters = listOf(
                ParamDoc("FDVar", "A finite domain variable"),
                ParamDoc("Elm", "An integer to check")
            )
        ),
        PicatDocEntry(
            name = "fd_disjoint",
            signature = "fd_disjoint(FDVar1, FDVar2)/2",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "True if FDVar1's domain and FDVar2's domain are disjoint (have no common values).",
            parameters = listOf(
                ParamDoc("FDVar1", "First finite domain variable"),
                ParamDoc("FDVar2", "Second finite domain variable")
            )
        ),
        PicatDocEntry(
            name = "fd_set_false",
            signature = "fd_set_false(FDVar, Elm)/2",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Excludes element Elm from the domain of FDVar. " +
                "If this creates a hole, the domain changes to bit-vector representation. Only in cp module.",
            parameters = listOf(
                ParamDoc("FDVar", "A finite domain variable"),
                ParamDoc("Elm", "The element to exclude")
            )
        ),
        PicatDocEntry(
            name = "fd_vector_min_max",
            signature = "fd_vector_min_max(Min, Max)/2",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "When arguments are integers, specifies the range of bit vectors. " +
                "When arguments are variables, binds them to current bounds. Default range is -3200..3200.",
            parameters = listOf(
                ParamDoc("Min", "Minimum bound or output variable"),
                ParamDoc("Max", "Maximum bound or output variable")
            )
        ),
        PicatDocEntry(
            name = "new_dvar",
            signature = "new_dvar()/0",
            module = "cp",
            category = DocCategory.DOMAIN_FUNCTION,
            description = "Creates a new domain variable with the default domain " +
                "(-72057594037927935..72057594037927935 on 64-bit, -268435455..268435455 on 32-bit).",
            examples = listOf("X = new_dvar()")
        )
    )

    private fun tableConstraints(): List<PicatDocEntry> = listOf(
        PicatDocEntry(
            name = "table_in",
            signature = "table_in(DVars, R)/2",
            module = "cp",
            category = DocCategory.TABLE_CONSTRAINT,
            description = "Positive table constraint: the tuple DVars must be one of the tuples in R. " +
                "DVars is a tuple {X1,...,Xn} or list of tuples. R is a list of allowed tuples, " +
                "where * denotes don't-care.",
            parameters = listOf(
                ParamDoc("DVars", "A tuple or list of tuples of domain variables"),
                ParamDoc("R", "A list of allowed tuples")
            ),
            examples = listOf(
                "table_in({X,Y}, [{1,2}, {2,3}, {3,4}])",
                "table_in([{A,B}, {C,D}], [{1,*}, {*,2}])"
            )
        ),
        PicatDocEntry(
            name = "table_notin",
            signature = "table_notin(DVars, R)/2",
            module = "cp",
            category = DocCategory.TABLE_CONSTRAINT,
            description = "Negative table constraint: the tuple DVars must NOT be any of the tuples in R.",
            parameters = listOf(
                ParamDoc("DVars", "A tuple or list of tuples of domain variables"),
                ParamDoc("R", "A list of disallowed tuples")
            )
        )
    )

    @Suppress("LongMethod")
    private fun arithmeticFunctions(): List<PicatDocEntry> = listOf(
        PicatDocEntry(
            name = "cond",
            signature = "cond(BoolConstr, ThenExp, ElseExp)",
            module = "cp",
            category = DocCategory.ARITHMETIC_FUNCTION,
            description = "Conditional expression in constraints. Equivalent to: " +
                "BoolConstr*ThenExp + (1-BoolConstr)*ElseExp",
            parameters = listOf(
                ParamDoc("BoolConstr", "A Boolean constraint (0 or 1)"),
                ParamDoc("ThenExp", "Expression when true"),
                ParamDoc("ElseExp", "Expression when false")
            )
        ),
        PicatDocEntry(
            name = "count",
            signature = "count(V, DVars)",
            module = "cp",
            category = DocCategory.ARITHMETIC_FUNCTION,
            description = "Returns the number of times V occurs in DVars. Can be used in arithmetic constraints.",
            parameters = listOf(
                ParamDoc("V", "Value to count"),
                ParamDoc("DVars", "List of domain variables")
            ),
            examples = listOf("count(1, [X,Y,Z]) #= 2  % exactly two 1s")
        ),
        PicatDocEntry(
            name = "sum",
            signature = "sum(DVars)",
            module = "cp",
            category = DocCategory.ARITHMETIC_FUNCTION,
            description = "Returns the sum of all values in DVars. Can be used in arithmetic constraints.",
            parameters = listOf(ParamDoc("DVars", "List of domain variables")),
            examples = listOf("sum([X,Y,Z]) #= 10")
        ),
        PicatDocEntry(
            name = "prod",
            signature = "prod(DVars)",
            module = "cp",
            category = DocCategory.ARITHMETIC_FUNCTION,
            description = "Returns the product of all values in DVars. Can be used in arithmetic constraints.",
            parameters = listOf(ParamDoc("DVars", "List of domain variables"))
        ),
        PicatDocEntry(
            name = "min",
            signature = "min(DVars)",
            module = "cp",
            category = DocCategory.ARITHMETIC_FUNCTION,
            description = "Returns the minimum value in DVars. Can be used in arithmetic constraints.",
            parameters = listOf(ParamDoc("DVars", "List of domain variables")),
            examples = listOf("min([X,Y,Z]) #>= 5")
        ),
        PicatDocEntry(
            name = "max",
            signature = "max(DVars)",
            module = "cp",
            category = DocCategory.ARITHMETIC_FUNCTION,
            description = "Returns the maximum value in DVars. Can be used in arithmetic constraints.",
            parameters = listOf(ParamDoc("DVars", "List of domain variables")),
            examples = listOf("max([X,Y,Z]) #=< 100")
        )
    )

    @Suppress("LongMethod")
    private fun globalConstraints(): List<PicatDocEntry> = listOf(
        PicatDocEntry(
            name = "all_different",
            signature = "all_different(FDVars)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Constrains all variables in FDVars to have pairwise different values. " +
                "Compiled into inequality constraints.",
            parameters = listOf(ParamDoc("FDVars", "A list or array of finite domain variables")),
            examples = listOf("Vars = [X,Y,Z], Vars :: 1..3, all_different(Vars)")
        ),
        PicatDocEntry(
            name = "all_distinct",
            signature = "all_distinct(FDVars)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Same as all_different but maintains a higher level of consistency (for cp module). " +
                "May be faster for some problems but slower for others due to checking overhead.",
            parameters = listOf(ParamDoc("FDVars", "A list or array of finite domain variables"))
        ),
        PicatDocEntry(
            name = "all_different_except_0",
            signature = "all_different_except_0(FDVars)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "All non-zero values in FDVars must be different. Zeros are allowed to repeat.",
            parameters = listOf(ParamDoc("FDVars", "A list or array of finite domain variables"))
        ),
        PicatDocEntry(
            name = "assignment",
            signature = "assignment(FDVars1, FDVars2)/2",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "FDVars2 is a dual assignment of FDVars1: if the i-th element of FDVars1 is j, " +
                "then the j-th element of FDVars2 is i.",
            parameters = listOf(
                ParamDoc("FDVars1", "First list of domain variables"),
                ParamDoc("FDVars2", "Second list (dual assignment)")
            )
        ),
        PicatDocEntry(
            name = "at_least",
            signature = "at_least(N, L, V)/3",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "At least N elements in L are equal to V.",
            parameters = listOf(
                ParamDoc("N", "Minimum count"),
                ParamDoc("L", "List of domain variables"),
                ParamDoc("V", "Value to count")
            )
        ),
        PicatDocEntry(
            name = "at_most",
            signature = "at_most(N, L, V)/3",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "At most N elements in L are equal to V.",
            parameters = listOf(
                ParamDoc("N", "Maximum count"),
                ParamDoc("L", "List of domain variables"),
                ParamDoc("V", "Value to count")
            )
        ),
        PicatDocEntry(
            name = "exactly",
            signature = "exactly(N, L, V)/3",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Exactly N elements in L are equal to V.",
            parameters = listOf(
                ParamDoc("N", "Exact count"),
                ParamDoc("L", "List of domain variables"),
                ParamDoc("V", "Value to count")
            )
        ),
        PicatDocEntry(
            name = "circuit",
            signature = "circuit(FDVars)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The variables form a Hamiltonian cycle. If FDVars = [X1,...,Xn] where each Xi " +
                "has domain 1..N, then 1->v1, 2->v2, ..., n->vn forms a single cycle visiting all nodes.",
            parameters = listOf(ParamDoc("FDVars", "A list of domain variables with domain 1..N")),
            examples = listOf(
                "circuit([X1,X2,X3,X4])  % [3,4,2,1] is valid, [2,1,4,3] is not (two sub-cycles)"
            )
        ),
        PicatDocEntry(
            name = "subcircuit",
            signature = "subcircuit(FDVars)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Like circuit, but not all vertices need to be in the circuit. " +
                "If Xi = i, then vertex i is not part of the circuit.",
            parameters = listOf(ParamDoc("FDVars", "A list of domain variables"))
        ),
        PicatDocEntry(
            name = "count",
            signature = "count(V, FDVars, Rel, N)/4",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The count of elements equal to V in FDVars has relation Rel with N. " +
                "Rel is one of: #=, #!=, #>, #>=, #<, #=<, #<=",
            parameters = listOf(
                ParamDoc("V", "Value to count"),
                ParamDoc("FDVars", "List of domain variables"),
                ParamDoc("Rel", "Relational operator"),
                ParamDoc("N", "Target count")
            ),
            examples = listOf("count(1, [X,Y,Z], #>=, 2)  % at least two 1s")
        ),
        PicatDocEntry(
            name = "cumulative",
            signature = "cumulative(Starts, Durations, Resources, Limit)/4",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Scheduling constraint: tasks with given start times, durations, and resource " +
                "requirements must not exceed the resource Limit at any time.",
            parameters = listOf(
                ParamDoc("Starts", "List of start time variables"),
                ParamDoc("Durations", "List of duration values/variables"),
                ParamDoc("Resources", "List of resource requirements"),
                ParamDoc("Limit", "Maximum resource capacity")
            )
        ),
        PicatDocEntry(
            name = "serialized",
            signature = "serialized(Starts, Durations)/2",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Tasks are non-overlapping. Equivalent to cumulative with all resources=1 and limit=1.",
            parameters = listOf(
                ParamDoc("Starts", "List of start time variables"),
                ParamDoc("Durations", "List of durations")
            )
        ),
        PicatDocEntry(
            name = "element",
            signature = "element(I, List, V)/3",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The I-th element (1-based) of List is V. I, V are domain variables.",
            parameters = listOf(
                ParamDoc("I", "Index variable (1-based)"),
                ParamDoc("List", "List of domain variables"),
                ParamDoc("V", "Value variable")
            )
        ),
        PicatDocEntry(
            name = "element0",
            signature = "element0(I, List, V)/3",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Like element but 0-based indexing.",
            parameters = listOf(
                ParamDoc("I", "Index variable (0-based)"),
                ParamDoc("List", "List of domain variables"),
                ParamDoc("V", "Value variable")
            )
        ),
        PicatDocEntry(
            name = "matrix_element",
            signature = "matrix_element(Matrix, I, J, V)/4",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The entry at <I,J> in Matrix is V (1-based indexing).",
            parameters = listOf(
                ParamDoc("Matrix", "2D array of domain variables"),
                ParamDoc("I", "Row index variable"),
                ParamDoc("J", "Column index variable"),
                ParamDoc("V", "Value variable")
            )
        ),
        PicatDocEntry(
            name = "global_cardinality",
            signature = "global_cardinality(List, Pairs)/2",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Each element of List equals some key, and for each Key-Count pair, " +
                "exactly Count elements equal Key.",
            parameters = listOf(
                ParamDoc("List", "List of domain variables"),
                ParamDoc("Pairs", "List of Key-Count pairs")
            ),
            examples = listOf("global_cardinality([X,Y,Z], [1-C1, 2-C2])  % C1 ones, C2 twos")
        ),
        PicatDocEntry(
            name = "nvalue",
            signature = "nvalue(N, List)/2",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The number of distinct values in List is N.",
            parameters = listOf(
                ParamDoc("N", "Count of distinct values"),
                ParamDoc("List", "List of domain variables")
            )
        ),
        PicatDocEntry(
            name = "increasing",
            signature = "increasing(L)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The sequence L is in non-strictly increasing order.",
            parameters = listOf(ParamDoc("L", "A list or array"))
        ),
        PicatDocEntry(
            name = "increasing_strict",
            signature = "increasing_strict(L)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The sequence L is in strictly increasing order.",
            parameters = listOf(ParamDoc("L", "A list or array"))
        ),
        PicatDocEntry(
            name = "decreasing",
            signature = "decreasing(L)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The sequence L is in non-strictly decreasing order.",
            parameters = listOf(ParamDoc("L", "A list or array"))
        ),
        PicatDocEntry(
            name = "decreasing_strict",
            signature = "decreasing_strict(L)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The sequence L is in strictly decreasing order.",
            parameters = listOf(ParamDoc("L", "A list or array"))
        ),
        PicatDocEntry(
            name = "lex_le",
            signature = "lex_le(L1, L2)/2",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Sequence L1 is lexicographically less than or equal to L2.",
            parameters = listOf(
                ParamDoc("L1", "First sequence"),
                ParamDoc("L2", "Second sequence")
            )
        ),
        PicatDocEntry(
            name = "lex_lt",
            signature = "lex_lt(L1, L2)/2",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Sequence L1 is lexicographically less than L2.",
            parameters = listOf(
                ParamDoc("L1", "First sequence"),
                ParamDoc("L2", "Second sequence")
            )
        ),
        PicatDocEntry(
            name = "scalar_product",
            signature = "scalar_product(A, X, Product)/3",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "The scalar product of A and X equals Product.",
            parameters = listOf(
                ParamDoc("A", "List of coefficients"),
                ParamDoc("X", "List of domain variables"),
                ParamDoc("Product", "Result variable")
            )
        ),
        PicatDocEntry(
            name = "diffn",
            signature = "diffn(RectangleList)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "No two rectangles in RectangleList overlap. Each rectangle is " +
                "[X1,..,Xn, S1,..,Sn] where Xi is starting coordinate and Si is size in dimension i.",
            parameters = listOf(ParamDoc("RectangleList", "List of rectangle specifications"))
        ),
        PicatDocEntry(
            name = "regular",
            signature = "regular(L, Q, S, M, Q0, F)/6",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "List L is accepted by the finite automaton with Q states, input alphabet 1..S, " +
                "transition matrix M, initial state Q0, and accepting states F.",
            parameters = listOf(
                ParamDoc("L", "Input list"),
                ParamDoc("Q", "Number of states"),
                ParamDoc("S", "Alphabet size"),
                ParamDoc("M", "Transition matrix Q x S -> 0..Q"),
                ParamDoc("Q0", "Initial state"),
                ParamDoc("F", "List of accepting states")
            )
        ),
        PicatDocEntry(
            name = "neqs",
            signature = "neqs(NeqList)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "NeqList is a list of X #!= Y constraints. Extracts all_distinct constraints " +
                "when possible for better propagation.",
            parameters = listOf(ParamDoc("NeqList", "List of inequality constraints"))
        ),
        PicatDocEntry(
            name = "disjunctive_tasks",
            signature = "disjunctive_tasks(Tasks)/1",
            module = "cp",
            category = DocCategory.GLOBAL_CONSTRAINT,
            description = "Tasks is a list of disj_tasks(S1,D1,S2,D2) terms ensuring tasks don't overlap.",
            parameters = listOf(ParamDoc("Tasks", "List of disjunctive task specifications"))
        )
    )

    private fun solverFunctions(): List<PicatDocEntry> = listOf(
        PicatDocEntry(
            name = "solve",
            signature = "solve(Vars)/1, solve(Options, Vars)/2",
            module = "cp",
            category = DocCategory.SOLVER_FUNCTION,
            description = "Labels the variables in Vars with values. Options control variable/value " +
                "selection and optimization. Can backtrack to find multiple solutions.",
            parameters = listOf(
                ParamDoc("Options", "List of solver options (ff, ffc, min, max, etc.)"),
                ParamDoc("Vars", "List or array of decision variables")
            ),
            examples = listOf(
                "solve(Vars)",
                "solve([ff], Vars)",
                "solve([ff, down], Vars)",
                "solve([\$min(Cost)], Vars)"
            ),
            seeAlso = listOf("indomain", "solve_all")
        ),
        PicatDocEntry(
            name = "solve_all",
            signature = "solve_all(Vars)/1, solve_all(Options, Vars)/2",
            module = "cp",
            category = DocCategory.SOLVER_FUNCTION,
            description = "Returns all solutions satisfying the constraints as a list.",
            parameters = listOf(
                ParamDoc("Options", "Solver options"),
                ParamDoc("Vars", "Decision variables")
            ),
            examples = listOf("Solutions = solve_all([X,Y,Z])")
        ),
        PicatDocEntry(
            name = "indomain",
            signature = "indomain(Var)/1",
            module = "cp",
            category = DocCategory.SOLVER_FUNCTION,
            description = "Labels a single variable. Equivalent to solve([], [Var]). Only for cp module.",
            parameters = listOf(ParamDoc("Var", "A domain variable"))
        ),
        PicatDocEntry(
            name = "indomain_down",
            signature = "indomain_down(Var)/1",
            module = "cp",
            category = DocCategory.SOLVER_FUNCTION,
            description = "Labels a single variable trying values from largest to smallest. " +
                "Equivalent to solve([down], [Var]). Only for cp module.",
            parameters = listOf(ParamDoc("Var", "A domain variable"))
        ),
        PicatDocEntry(
            name = "solve_suspended",
            signature = "solve_suspended/0, solve_suspended(Options)/1",
            module = "cp",
            category = DocCategory.SOLVER_FUNCTION,
            description = "Labels remaining variables in suspended constraints after solve() " +
                "has labeled some variables. Only for cp module.",
            parameters = listOf(ParamDoc("Options", "Solver options"))
        )
    )

    @Suppress("LongMethod")
    private fun solverOptions(): List<PicatDocEntry> = listOf(
        // Variable ordering options
        PicatDocEntry(
            name = "ff",
            signature = "ff",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "First-fail variable ordering: select the variable with the smallest domain. " +
                "This is the most commonly used heuristic for constraint satisfaction.",
            examples = listOf("solve([ff], Vars)")
        ),
        PicatDocEntry(
            name = "ffc",
            signature = "ffc",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "First-fail with constraint count: select variable with smallest domain, " +
                "breaking ties by choosing the variable attached to the most constraints.",
            examples = listOf("solve([ffc], Vars)")
        ),
        PicatDocEntry(
            name = "ffd",
            signature = "ffd",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "First-fail with degree: select variable with smallest domain, " +
                "breaking ties by choosing the variable connected to the most uninstantiated variables.",
            examples = listOf("solve([ffd], Vars)")
        ),
        PicatDocEntry(
            name = "forward",
            signature = "forward",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Choose variables in the given order, from left to right.",
            examples = listOf("solve([forward], Vars)")
        ),
        PicatDocEntry(
            name = "backward",
            signature = "backward",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Reverse the variable list before labeling.",
            examples = listOf("solve([backward], Vars)")
        ),
        PicatDocEntry(
            name = "leftmost",
            signature = "leftmost",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Same as forward: choose variables from left to right.",
            examples = listOf("solve([leftmost], Vars)")
        ),
        PicatDocEntry(
            name = "constr",
            signature = "constr",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Order variables by the number of attached constraints (most constrained first).",
            examples = listOf("solve([constr], Vars)")
        ),
        PicatDocEntry(
            name = "degree",
            signature = "degree",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Order variables by degree (number of connected uninstantiated variables).",
            examples = listOf("solve([degree], Vars)")
        ),
        // Value ordering options
        PicatDocEntry(
            name = "down",
            signature = "down",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Try values from largest to smallest (descending order).",
            examples = listOf("solve([down], Vars)", "solve([ff, down], Vars)")
        ),
        PicatDocEntry(
            name = "updown",
            signature = "updown",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Try values nearest to the middle of the domain first.",
            examples = listOf("solve([updown], Vars)")
        ),
        PicatDocEntry(
            name = "split",
            signature = "split",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Bisect the domain, excluding the upper half first. Binary domain splitting.",
            examples = listOf("solve([split], Vars)")
        ),
        PicatDocEntry(
            name = "reverse_split",
            signature = "reverse_split",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Bisect the domain, excluding the lower half first.",
            examples = listOf("solve([reverse_split], Vars)")
        ),
        PicatDocEntry(
            name = "inout",
            signature = "inout",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Reorder variables inside-out. [X1,X2,X3,X4,X5] becomes [X3,X2,X4,X1,X5].",
            examples = listOf("solve([inout], Vars)")
        ),
        // Random options
        PicatDocEntry(
            name = "rand",
            signature = "rand",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Both variables and values are selected randomly during labeling.",
            examples = listOf("solve([rand], Vars)")
        ),
        PicatDocEntry(
            name = "rand_var",
            signature = "rand_var",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Variables are selected randomly during labeling.",
            examples = listOf("solve([rand_var], Vars)")
        ),
        PicatDocEntry(
            name = "rand_val",
            signature = "rand_val",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Values are selected randomly during labeling.",
            examples = listOf("solve([rand_val], Vars)")
        ),
        // Min/max variable selection
        PicatDocEntry(
            name = "min",
            signature = "min (option)",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Select variable whose domain has the smallest lower bound, " +
                "breaking ties by smallest domain size.",
            examples = listOf("solve([min], Vars)")
        ),
        PicatDocEntry(
            name = "max",
            signature = "max (option)",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Select variable whose domain has the largest upper bound, " +
                "breaking ties by smallest domain size.",
            examples = listOf("solve([max], Vars)")
        ),
        // Optimization options
        PicatDocEntry(
            name = "\$min",
            signature = "\$min(Var)",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Minimize the value of Var during search. Finds optimal solution.",
            parameters = listOf(ParamDoc("Var", "The variable to minimize")),
            examples = listOf("solve([\$min(Cost)], Vars)")
        ),
        PicatDocEntry(
            name = "\$max",
            signature = "\$max(Var)",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Maximize the value of Var during search. Finds optimal solution.",
            parameters = listOf(ParamDoc("Var", "The variable to maximize")),
            examples = listOf("solve([\$max(Profit)], Vars)")
        ),
        PicatDocEntry(
            name = "\$limit",
            signature = "\$limit(N)",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Search up to N solutions. Limits the number of solutions found.",
            parameters = listOf(ParamDoc("N", "Maximum number of solutions")),
            examples = listOf("solve([\$limit(10)], Vars)")
        ),
        PicatDocEntry(
            name = "\$report",
            signature = "\$report(Call)",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Execute Call each time a better solution is found during optimization. " +
                "Not available with mip module.",
            parameters = listOf(ParamDoc("Call", "Goal to execute on improvement")),
            examples = listOf("solve([\$min(Cost), \$report(println(Cost))], Vars)")
        ),
        // SAT options
        PicatDocEntry(
            name = "dump",
            signature = "dump, dump(File)",
            module = "sat",
            category = DocCategory.SOLVER_OPTION,
            description = "Dump the CNF code to stdout or to File (for sat module).",
            examples = listOf("solve([dump], Vars)", "solve([dump(\"problem.cnf\")], Vars)")
        ),
        PicatDocEntry(
            name = "seq",
            signature = "seq",
            module = "sat",
            category = DocCategory.SOLVER_OPTION,
            description = "Use sequential search to find optimal answer (for sat module).",
            examples = listOf("solve([seq, \$min(Cost)], Vars)")
        ),
        // MIP solver options
        PicatDocEntry(
            name = "gurobi",
            signature = "gurobi",
            module = "mip",
            category = DocCategory.SOLVER_OPTION,
            description = "Use the Gurobi MIP solver. Requires gurobi_cl command available.",
            examples = listOf("solve([gurobi], Vars)")
        ),
        PicatDocEntry(
            name = "cbc",
            signature = "cbc",
            module = "mip",
            category = DocCategory.SOLVER_OPTION,
            description = "Use the CBC MIP solver. Requires cbc command available.",
            examples = listOf("solve([cbc], Vars)")
        ),
        PicatDocEntry(
            name = "glpk",
            signature = "glpk",
            module = "mip",
            category = DocCategory.SOLVER_OPTION,
            description = "Use the GLPK MIP solver. Requires glpsol command available.",
            examples = listOf("solve([glpk], Vars)")
        ),
        PicatDocEntry(
            name = "scip",
            signature = "scip",
            module = "mip",
            category = DocCategory.SOLVER_OPTION,
            description = "Use the SCIP MIP solver. Note: SCIP only returns integer solutions.",
            examples = listOf("solve([scip], Vars)")
        ),
        // SMT solver options
        PicatDocEntry(
            name = "z3",
            signature = "z3",
            module = "smt",
            category = DocCategory.SOLVER_OPTION,
            description = "Use the Z3 SMT solver (default for smt module).",
            examples = listOf("solve([z3], Vars)")
        ),
        PicatDocEntry(
            name = "cvc4",
            signature = "cvc4",
            module = "smt",
            category = DocCategory.SOLVER_OPTION,
            description = "Use the CVC4 SMT solver.",
            examples = listOf("solve([cvc4], Vars)")
        ),
        PicatDocEntry(
            name = "label",
            signature = "label(CallName)",
            module = "cp",
            category = DocCategory.SOLVER_OPTION,
            description = "Use user-defined CallName(V) to label each variable V.",
            parameters = listOf(ParamDoc("CallName", "Name of labeling predicate")),
            examples = listOf("solve([label(my_label)], Vars)")
        )
    )
}
