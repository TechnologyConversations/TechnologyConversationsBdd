package models

import models.jbehave.JBehaveComposites
import models.file.FileTraitComposites

class Composites(val dir: String, val path: String) extends JBehaveComposites with FileTraitComposites {
}

object Composites {
  def apply(dir: String): Composites = new Composites(dir, "")
  def apply(dir: String, path: String): Composites = new Composites(dir, path)
}
