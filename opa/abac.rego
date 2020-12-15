package abac

default allow_action = false

allow_action {
  input.action == "read"
  input.resource.secret == false
  input.subject.authorities[_] == "ROLE_READER"
}

allow_action {
  input.action == "read"
  input.resource.author == input.subject.name
  input.subject.authorities[_] == "ROLE_READER"
}

allow_action {
  input.action == "write"
  input.resource.author == input.subject.name
  input.subject.authorities[_] == "ROLE_WRITER"
}