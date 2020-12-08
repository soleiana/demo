package authz

default allow = false

allow {
  input.name == "alice"
  input.method == "POST"
  input.path = ["document"]
  input.authorities[_] == "ROLE_WRITER"
}

allow {
  input.method == "GET"
  input.path = ["document", _]
  input.authorities[_] == "ROLE_READER"
}