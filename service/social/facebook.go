package social

type Facebook struct{}

func (s *Facebook) FollowerCount() (int64, error) {
	return int64(50466), nil
}
