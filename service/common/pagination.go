package common

import (
	"errors"
	pb "github.com/OSAlt/geekbeacon/service/autogen"
)

// Pagination type that is used to calculate the offset and capture payload size and such
type Pagination struct {
	PageNumber int32
	PageSize int32
	Limit int32

}

func (s *Pagination) Init(request *pb.SocialType) {
	s.PageNumber = request.PageNum
	s.PageSize = request.PageSize
	s.Limit = request.Limit
}

func (s *Pagination) IsPaginationValid() bool {
	if s.PageNumber <= 0 || s.PageSize <= 0 || s.Limit < 0 {
		return false
	}

	return true

}

func (s *Pagination) GetOffset() (int32, error) {
	if s.IsPaginationValid() != true {
		return 0, errors.New("missing Parameter, cannot determined offset")
	}
	if s.PageNumber == 1 {
		return 0, nil
	}

	return  (s.PageNumber - 1) * s.PageSize + 1, nil

}
