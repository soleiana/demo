package rbac

default allow_method = false

allow_method {
  names := {"alice", "bob"}
  names[input.name] #check if name is in set
  input.method == "POST"
  input.path = ["document"]
  input.authorities[_] == "ROLE_WRITER"
}

allow_method {
  input.method == "GET"
  input.path = ["document", _]
  input.authorities[_] == "ROLE_READER"
}