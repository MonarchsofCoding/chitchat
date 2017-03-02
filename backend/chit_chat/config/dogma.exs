use Mix.Config
alias Dogma.Rule

config :dogma,

  # Select a set of rules as a base
  rule_set: Dogma.RuleSet.All,

  # Pick paths not to lint
  exclude: [
    ~r(\Atest/),
    ~r(\Arel/),
    ~r(\Alib/),
    ~r(\Aconfig/),
    ~r(\Amix.exs),
  ],

  # Override an existing rule configuration
  override: [
  ]
