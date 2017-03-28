from invoke import task

@task
def build(ctx):
  """
  Generates a PDF from the latex
  """
  ctx.run("pdflatex final-report")
  ctx.run("bibtex final-report")
  ctx.run("pdflatex final-report")
  ctx.run("pdflatex final-report")
  pass
