package models

class Story(val path: String) extends JBehaveStory with FileStory { }

object Story {
  def apply(path: String) = new Story(path)
}