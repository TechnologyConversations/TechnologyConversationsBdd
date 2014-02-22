package models

class Story(val path: String) extends JBehaveStory with FileStory { }

object Story {
  def apply(): Story = new Story("")
  def apply(path: String): Story = new Story(path)
}
